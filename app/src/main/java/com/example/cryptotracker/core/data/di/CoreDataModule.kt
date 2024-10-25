package com.example.cryptotracker.core.data.di

import com.example.cryptotracker.core.data.dispatchers.AppDefaultDispatchers
import com.example.cryptotracker.core.data.network.HttpClientFactoryImpl
import com.example.cryptotracker.core.domain.dispatchers.AppDispatchers
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coreDataModule = module {
    single<AppDispatchers> { AppDefaultDispatchers }
    single<CoroutineDispatcher>(named(AppDispatchers.DISPATCHER_IO)) { AppDefaultDispatchers.ioDispatcher }
    single<CoroutineDispatcher>(named(AppDispatchers.DISPATCHER_DEFAULT)) { AppDefaultDispatchers.defaultDispatcher }
    single<CoroutineDispatcher>(named(AppDispatchers.DISPATCHER_UI)) { AppDefaultDispatchers.mainDispatcher }
    single<HttpClient> { HttpClientFactoryImpl().create(engine = CIO.create()) }
}