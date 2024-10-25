package com.example.cryptotracker.crypto.data.mappers

import com.example.cryptotracker.crypto.data.dto.CoinDto
import com.example.cryptotracker.crypto.domain.Coin

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