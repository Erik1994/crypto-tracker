package com.example.cryptotracker.core.data.network

import com.example.cryptotracker.BuildConfig

sealed class Endpoint(val url: String) {

    data object Assets : Endpoint("${BASE_URL}assets")

    data class History(val id: String): Endpoint("${BASE_URL}assets/$id/history")

    companion object {
        const val BASE_URL = BuildConfig.BASE_URL
        const val ASSETS = "assets"
        const val HISTORY = "history"
    }
}