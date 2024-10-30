package com.example.cryptotracker.crypto.presentation.models

import androidx.annotation.DrawableRes
import com.example.cryptotracker.crypto.presentation.coin_detail.DataPoint

data class CoinUi(
    val id: String,
    val rank: Int,
    val name: String,
    val symbol: String,
    val marketCapsUsd: DisplayableNumber,
    val priceUsd: DisplayableNumber,
    val changePercent24Hr: DisplayableNumber,
    @DrawableRes val iconRes: Int,
    val coinPriceHistory: List<DataPoint> = emptyList(),
)

data class DisplayableNumber(
    val value: Double,
    val formatted: String
)
