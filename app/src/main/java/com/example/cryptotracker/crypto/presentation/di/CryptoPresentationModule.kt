package com.example.cryptotracker.crypto.presentation.di

import com.example.cryptotracker.crypto.presentation.coin_list.CoinListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val cryptoPresentationModule = module {
    viewModelOf(::CoinListViewModel)
}