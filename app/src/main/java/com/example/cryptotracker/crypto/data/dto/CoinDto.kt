package com.example.cryptotracker.crypto.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoinDto(
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "rank")
    val rank: Int,
    @SerialName(value = "name")
    val name: String,
    @SerialName(value = "symbol")
    val symbol: String,
    @SerialName(value = "marketCapUsd")
    val marketCapsUsd: Double,
    @SerialName(value = "priceUsd")
    val priceUsd: Double,
    @SerialName(value = "changePercent24Hr")
    val changePercent24Hr: Double
)
