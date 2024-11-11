package com.example.cryptotracker.crypto

import com.example.cryptotracker.core.data.network.CryptoApi
import com.example.cryptotracker.core.data.network.mapToResult
import com.example.cryptotracker.core.domain.util.NetworkError
import com.example.cryptotracker.core.domain.util.Result
import com.example.cryptotracker.crypto.data.mappers.toCoin
import com.example.cryptotracker.crypto.domain.Coin
import com.example.cryptotracker.crypto.domain.CoinDataSourceRx
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class CoinDataSourceRxImpl(
    private val cryptoApi: CryptoApi
) : CoinDataSourceRx {
    override fun getCoins(): Single<Result<List<Coin>, NetworkError>> {
        return cryptoApi.getAssets()
            .mapToResult { response ->
                response.data.map {
                    it.toCoin()
                }
            }.subscribeOn(Schedulers.newThread())
    }
}