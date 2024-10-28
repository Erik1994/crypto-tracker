package com.example.cryptotracker.crypto.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.cryptotracker.core.data.network.Endpoint
import com.example.cryptotracker.core.data.network.get
import com.example.cryptotracker.core.domain.util.NetworkError
import com.example.cryptotracker.core.domain.util.Result
import com.example.cryptotracker.core.domain.util.map
import com.example.cryptotracker.crypto.data.dto.CoinsHistoryDto
import com.example.cryptotracker.crypto.data.dto.CoinsResponseDto
import com.example.cryptotracker.crypto.data.mappers.toCoin
import com.example.cryptotracker.crypto.data.mappers.toCoinPrice
import com.example.cryptotracker.crypto.domain.Coin
import com.example.cryptotracker.crypto.domain.CoinDataSource
import com.example.cryptotracker.crypto.domain.CoinPrice
import io.ktor.client.HttpClient
import java.time.ZoneId
import java.time.ZonedDateTime

class CoinRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : CoinDataSource {
    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        return httpClient.get<CoinsResponseDto>(
            endpoint = Endpoint.Assets
        ).map { response ->
            response.data.map { it.toCoin() }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getCoinHistory(
        coinId: String,
        startTime: ZonedDateTime,
        endTime: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError> {
        val (startMillis, endMillis) = (
                startTime.withZoneSameInstant(ZoneId.of("UTC"))
                    .toInstant()
                    .toEpochMilli() to endTime.withZoneSameInstant(ZoneId.of("UTC"))
                    .toInstant()
                    .toEpochMilli()
                )
        return httpClient.get<CoinsHistoryDto>(
            endpoint = Endpoint.History(id = coinId),
            params = mapOf(
                "interval" to "h6",
                "start" to startMillis,
                "end" to startMillis
            )
        ).map { response ->
            response.data.map { it.toCoinPrice() }
        }
    }
}