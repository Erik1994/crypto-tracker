package com.example.cryptotracker.crypto.presentation.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.core.domain.util.onError
import com.example.cryptotracker.core.domain.util.onSuccess
import com.example.cryptotracker.core.presentation.mappers.toUiText
import com.example.cryptotracker.crypto.domain.CoinDataSource
import com.example.cryptotracker.crypto.presentation.mappers.toCoinUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoinListViewModel(
    private val coinDataSource: CoinDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(CoinListState())
    val state = _state
        .onStart { loadCoins() }
        .stateIn(
            scope = viewModelScope,
            SharingStarted.Lazily,
            initialValue = CoinListState()
        )

    private val _events = Channel<CoinListEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: CoinListAction) {
        when (action) {
            is CoinListAction.OnCoinClick -> {}
            CoinListAction.OnRefresh -> loadCoins(isRefreshing = true)
        }
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
}