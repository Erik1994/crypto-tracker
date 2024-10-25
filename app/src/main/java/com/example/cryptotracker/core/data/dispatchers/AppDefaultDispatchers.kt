package com.example.cryptotracker.core.data.dispatchers

import com.example.cryptotracker.core.domain.dispatchers.AppDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object AppDefaultDispatchers: AppDispatchers {
    override val ioDispatcher: CoroutineDispatcher by lazy { Dispatchers.IO }
    override val mainDispatcher: CoroutineDispatcher by lazy { Dispatchers.Main.immediate }
    override val defaultDispatcher: CoroutineDispatcher by lazy { Dispatchers.Default }
}