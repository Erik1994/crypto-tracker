package com.example.cryptotracker.crypto.presentation.coin_list

import com.example.cryptotracker.core.presentation.util.UiText

sealed interface CoinListEvent {
    data class Error(val message: UiText) : CoinListEvent
}