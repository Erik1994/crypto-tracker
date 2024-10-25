package com.example.cryptotracker.core.data.network

import com.example.cryptotracker.BuildConfig

sealed class Endpoint(val url: String) {



    private companion object {
        const val BASE_URL = BuildConfig.BASE_URL
    }
}