@file:OptIn(ExperimentalLayoutApi::class)

package com.example.cryptotracker.crypto.presentation.coin_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cryptotracker.R
import com.example.cryptotracker.crypto.presentation.coin_detail.components.CoinInfoCard
import com.example.cryptotracker.crypto.presentation.coin_list.CoinListState
import com.example.cryptotracker.crypto.presentation.coin_list.components.previewCoin
import com.example.cryptotracker.crypto.presentation.mappers.toDisplayableNumber
import com.example.cryptotracker.ui.dimension.LocalDimensions
import com.example.cryptotracker.ui.theme.CryptoTrackerTheme
import com.example.cryptotracker.ui.theme.greenBackground

@Composable
fun CoinDetailScreen(
    modifier: Modifier = Modifier,
    state: CoinListState,
) {
    val contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val dimensions = LocalDimensions.current
    if (state.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (state.selectedCoinUi != null) {
        val coin = state.selectedCoinUi
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(dimensions.dimenMedium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(coin.iconRes),
                contentDescription = coin.name,
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = coin.name,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                color = contentColor,
                textAlign = TextAlign.Center
            )
            Text(
                text = coin.symbol,
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                color = contentColor,
                textAlign = TextAlign.Center
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CoinInfoCard(
                    title = stringResource(R.string.market_cap),
                    formattedPrice = "$ ${coin.marketCapsUsd.formatted}",
                    icon = ImageVector.vectorResource(R.drawable.stock)
                )
                CoinInfoCard(
                    title = stringResource(R.string.price),
                    formattedPrice = "$ ${coin.priceUsd.formatted}",
                    icon = ImageVector.vectorResource(R.drawable.dollar)
                )
                val absoluteChangeFormatted =
                    (coin.priceUsd.value * (coin.changePercent24Hr.value / 100)).toDisplayableNumber()
                val isPositive = coin.changePercent24Hr.value > 0.0
                val cardInfoContentColor = if (isPositive) {
                    if (isSystemInDarkTheme()) Color.Green else greenBackground
                } else MaterialTheme.colorScheme.error
                CoinInfoCard(
                    title = stringResource(R.string.cange_last_24h),
                    formattedPrice = "$ ${absoluteChangeFormatted.formatted}",
                    icon = if (isPositive) {
                        ImageVector.vectorResource(R.drawable.trending)
                    } else ImageVector.vectorResource(R.drawable.trending_down),
                    contentColor = cardInfoContentColor
                )
            }
            AnimatedVisibility(visible = coin.coinPriceHistory.isNotEmpty()) {
                var selectedDataPoint by remember {
                    mutableStateOf<DataPoint?>(null)
                }
                var labelWidth by remember {
                    mutableFloatStateOf(0f)
                }
                var totalChartWidth by remember {
                    mutableFloatStateOf(0f)
                }
                val amountOfVisibleDataPoints = if(labelWidth > 0) {
                    ((totalChartWidth - 2.5 * labelWidth) / labelWidth).toInt()
                } else {
                    0
                }
                val startIndex = (coin.coinPriceHistory.lastIndex - amountOfVisibleDataPoints)
                    .coerceAtLeast(0)
                LineChart(
                    dataPoints = coin.coinPriceHistory,
                    style = ChartStyle(
                        chartLineColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.secondary.copy(
                            alpha = 0.3f
                        ),
                        selectedColor = MaterialTheme.colorScheme.primary,
                        helperLinesThicknessPx = 5f,
                        axisLinesThicknessPx = 5f,
                        labelFontSize = 14.sp,
                        minYLabelSpacing = 25.dp,
                        verticalPadding = 8.dp,
                        horizontalPadding = 8.dp,
                        xAxisLabelSpacing = 8.dp
                    ),
                    visibleDataPointsIndices = startIndex..coin.coinPriceHistory.lastIndex,
                    unit = "$",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f)
                        .onSizeChanged { totalChartWidth = it.width.toFloat() },
                    selectedDataPoint = selectedDataPoint,
                    onSelectedDataPoint = {
                        selectedDataPoint = it
                    },
                    onXLabelWidthChange = { labelWidth = it }
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun CoinDetailScreenPreview() {
    CryptoTrackerTheme {
        CoinDetailScreen(
            state = CoinListState(
                selectedCoinUi = previewCoin
            ),
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}