package com.example.cryptotracker.crypto.data.di

import com.example.cryptotracker.crypto.data.CoinRemoteDataSourceImpl
import com.example.cryptotracker.crypto.domain.CoinDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val cryptoDataModule = module {
    singleOf(::CoinRemoteDataSourceImpl).bind<CoinDataSource>()
}