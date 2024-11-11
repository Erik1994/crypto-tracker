package com.example.cryptotracker.crypto.presentation.coin_list

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.util.trace
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.core.domain.util.Result
import com.example.cryptotracker.core.domain.util.onError
import com.example.cryptotracker.core.domain.util.onSuccess
import com.example.cryptotracker.core.presentation.mappers.toUiText
import com.example.cryptotracker.core.presentation.util.UiText
import com.example.cryptotracker.crypto.domain.CoinDataSource
import com.example.cryptotracker.crypto.domain.CoinDataSourceRx
import com.example.cryptotracker.crypto.presentation.coin_detail.DataPoint
import com.example.cryptotracker.crypto.presentation.mappers.toCoinUi
import com.example.cryptotracker.crypto.presentation.models.CoinUi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.disposables.DisposableContainer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CoinListViewModel(
    private val coinDataSource: CoinDataSource,
    private val coinDataSourceRx: CoinDataSourceRx
) : ViewModel() {

    private val disposableContainer = CompositeDisposable()
    private val _state = MutableStateFlow(CoinListState())
    val state = _state
        .onStart { loadCoinsRx() }
        .stateIn(
            scope = viewModelScope,
            SharingStarted.Lazily,
            initialValue = CoinListState()
        )

    private val _events = Channel<CoinListEvent>()
    val events = _events.receiveAsFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun onAction(action: CoinListAction) {
        when (action) {
            is CoinListAction.OnCoinClick -> selectCoin(action.coinUi)
            CoinListAction.OnRefresh -> loadCoinsRx(isRefreshing = true)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun selectCoin(coin: CoinUi) {
        _state.update { it.copy(selectedCoinUi = coin) }
        viewModelScope.launch {
            coinDataSource.getCoinHistory(
                coinId = coin.id,
                startTime = ZonedDateTime.now().minusDays(5),
                endTime = ZonedDateTime.now()
            )
                .onSuccess { history ->
                    val dataPoints = history
                        .sortedBy { it.dateTime }
                        .map {
                            DataPoint(
                                x = it.dateTime.hour.toFloat(),
                                y = it.priceUsd.toFloat(),
                                xLabel = DateTimeFormatter
                                    .ofPattern("ha\nM/d")
                                    .format(it.dateTime)
                            )
                        }
                    _state.update {
                        it.copy(
                            selectedCoinUi = it.selectedCoinUi?.copy(
                                coinPriceHistory = dataPoints
                            )
                        )
                    }
                }
                .onError { error ->
                    _events.send(CoinListEvent.Error(message = error.toUiText()))
                }
        }
    }

    private fun loadCoinsRx(isRefreshing: Boolean = false) {
        val disposable: Disposable = coinDataSourceRx.getCoins()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                if (isRefreshing) {
                    _state.update { it.copy(isRefreshing = true) }
                } else {
                    _state.update { it.copy(isLoading = true) }
                }
            }
            .subscribe(
                { result ->
                    result
                        .onSuccess { coins ->
                            _state.update {
                                it.copy(
                                    coins = coins.map { coin -> coin.toCoinUi() }
                                )
                            }
                        }
                        .onError {
                            _events.trySend(CoinListEvent.Error(message = it.toUiText()))
                        }
                    if (isRefreshing) {
                        _state.update { it.copy(isRefreshing = false) }
                    } else {
                        _state.update { it.copy(isLoading = false) }
                    }
                },
                {
                    _events.trySend(CoinListEvent.Error(message = UiText.DynamicString(it.message.orEmpty())))
                },
            )
        disposableContainer.add(disposable)
    }

    private fun loadCoins(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            if (isRefreshing) {
                _state.update { it.copy(isRefreshing = true) }
                delay(500L)
            } else {
                _state.update { it.copy(isLoading = true) }
            }

            coinDataSource
                .getCoins()
                .onSuccess { coins ->
                    _state.update {
                        it.copy(
                            coins = coins.map { coin -> coin.toCoinUi() }
                        )
                    }
                    if (isRefreshing) {
                        _state.update { it.copy(isRefreshing = false) }
                    } else {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
                .onError { error ->
                    if (isRefreshing) {
                        _state.update { it.copy(isRefreshing = false) }
                    } else {
                        _state.update { it.copy(isLoading = false) }
                    }
                    _events.send(CoinListEvent.Error(message = error.toUiText()))
                }
        }
    }

    override fun onCleared() {
        disposableContainer.clear()
        super.onCleared()
    }
}