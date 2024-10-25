package com.example.cryptotracker.crypto.data

import com.example.cryptotracker.core.data.network.Endpoint
import com.example.cryptotracker.core.data.network.get
import com.example.cryptotracker.core.domain.util.NetworkError
import com.example.cryptotracker.core.domain.util.Result
import com.example.cryptotracker.core.domain.util.map
import com.example.cryptotracker.crypto.data.dto.CoinsResponseDto
import com.example.cryptotracker.crypto.data.mappers.toCoin
import com.example.cryptotracker.crypto.domain.Coin
import com.example.cryptotracker.crypto.domain.CoinDataSource
import io.ktor.client.HttpClient

class CoinRemoteDataSourceImpl(
    private val httpClient: HttpClient
): CoinDataSource {
    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        return httpClient.get<CoinsResponseDto>(
            endpoint = Endpoint.Assets
        ).map { response ->
            response.data.map { it.toCoin() }
        }
    }
}