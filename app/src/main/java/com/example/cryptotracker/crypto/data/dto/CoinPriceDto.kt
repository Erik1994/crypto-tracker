package com.example.cryptotracker.crypto.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinPriceDto(
    @SerialName(value = "priceUsd")
    val priceUsd: Double,
    @SerialName(value = "time")
    val time: Long
)
