package com.example.cryptotracker.crypto.presentation.mappers

import android.icu.text.NumberFormat
import com.example.cryptotracker.crypto.domain.Coin
import com.example.cryptotracker.crypto.presentation.models.CoinUi
import com.example.cryptotracker.crypto.presentation.models.DisplayableNumber
import com.example.cryptotracker.core.presentation.util.getDrawableIdForCoin
import java.util.Locale

fun Coin.toCoinUi(): CoinUi {
    return CoinUi(
        id = id,
        name = name,
        symbol = symbol,
        rank = rank,
        priceUsd = priceUsd.toDisplayableNumber(),
        marketCapsUsd = marketCapsUsd.toDisplayableNumber(),
        changePercent24Hr = changePercent24Hr.toDisplayableNumber(),
        iconRes = getDrawableIdForCoin(symbol = symbol)
    )
}

fun Double.toDisplayableNumber(): DisplayableNumber {
    val formatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    return DisplayableNumber(
        value = this,
        formatted = formatter.format(this)
    )
}