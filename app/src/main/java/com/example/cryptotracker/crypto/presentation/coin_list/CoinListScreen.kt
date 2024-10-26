@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.cryptotracker.crypto.presentation.coin_list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.cryptotracker.crypto.presentation.coin_list.components.CoinListItem
import com.example.cryptotracker.crypto.presentation.coin_list.components.previewCoin
import com.example.cryptotracker.crypto.presentation.mappers.toCoinUi
import com.example.cryptotracker.ui.dimension.LocalDimensions
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme

@Composable
fun CoinListScreen(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    state: CoinListState,
) {
    val dimensions = LocalDimensions.current
    val pullToRefreshState = rememberPullToRefreshState()
    if (state.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Log.d("DDDDDD", "CoinListScreen: ${state.isRefreshing}")
        PullToRefreshBox(
            modifier = modifier,
            state = pullToRefreshState,
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(dimensions.dimenSmall)
            ) {
                items(items = state.coins) { coinUi ->
                    CoinListItem(
                        coinUi = coinUi,
                        onClick = {},
                        modifier = Modifier.fillParentMaxWidth()
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun CoinListScreenPreview() {
    CryptoTrackerTheme {
        CoinListScreen(
            state = CoinListState(
                isLoading = false,
                coins = listOf(
                    previewCoin.toCoinUi(),
                    previewCoin.toCoinUi(),
                    previewCoin.toCoinUi(),
                    previewCoin.toCoinUi(),
                    previewCoin.toCoinUi(),
                )
            ),
            onRefresh = {},
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}
