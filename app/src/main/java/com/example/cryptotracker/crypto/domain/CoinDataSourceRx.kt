package com.example.cryptotracker.crypto.domain

import com.example.cryptotracker.core.domain.util.NetworkError
import com.example.cryptotracker.core.domain.util.Result
import io.reactivex.rxjava3.core.Single
import java.time.ZonedDateTime

interface CoinDataSourceRx {
    fun getCoins(): Single<Result<List<Coin>, NetworkError>>
}