package com.example.cryptotracker.core.data.network

import com.example.cryptotracker.crypto.data.dto.CoinsResponseDto
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET

interface CryptoApi {

    @GET("${Endpoint.BASE_URL}${Endpoint.ASSETS}")
    fun getAssets(): Single<Response<CoinsResponseDto>>
}