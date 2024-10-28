package com.example.cryptotracker.crypto.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinsHistoryDto(
    @SerialName(value = "data")
    val data: List<CoinPriceDto>
)
