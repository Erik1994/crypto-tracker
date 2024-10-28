package com.example.cryptotracker.crypto.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.cryptotracker.crypto.data.dto.CoinDto
import com.example.cryptotracker.crypto.data.dto.CoinPriceDto
import com.example.cryptotracker.crypto.domain.Coin
import com.example.cryptotracker.crypto.domain.CoinPrice
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

fun CoinDto.toCoin(): Coin {
    return Coin(
        id = id,
        name = name,
        rank = rank,
        symbol = symbol,
        marketCapsUsd = marketCapsUsd,
        priceUsd = priceUsd,
        changePercent24Hr = changePercent24Hr
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun CoinPriceDto.toCoinPrice(): CoinPrice {
    return CoinPrice(
        priceUsd = priceUsd,
        dateTime = Instant
            .ofEpochMilli(time)
            .atZone(ZoneId.of("UTC"))
    )
}