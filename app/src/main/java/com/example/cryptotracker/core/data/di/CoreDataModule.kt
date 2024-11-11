@file:Suppress("OPT_IN_USAGE")

package com.example.cryptotracker.core.data.di

import com.example.cryptotracker.BuildConfig
import com.example.cryptotracker.core.data.dispatchers.AppDefaultDispatchers
import com.example.cryptotracker.core.data.network.CryptoApi
import com.example.cryptotracker.core.data.network.HttpClientFactoryImpl
import com.example.cryptotracker.core.domain.dispatchers.AppDispatchers
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private val json = Json {
    ignoreUnknownKeys = true
}
val coreDataModule = module {
    single<AppDispatchers> { AppDefaultDispatchers }
    single<CoroutineDispatcher>(named(AppDispatchers.DISPATCHER_IO)) { AppDefaultDispatchers.ioDispatcher }
    single<CoroutineDispatcher>(named(AppDispatchers.DISPATCHER_DEFAULT)) { AppDefaultDispatchers.defaultDispatcher }
    single<CoroutineDispatcher>(named(AppDispatchers.DISPATCHER_UI)) { AppDefaultDispatchers.mainDispatcher }
    single<HttpClient> { HttpClientFactoryImpl().create(engine = CIO.create()) }

    factory<Interceptor> {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        }
    }

    factory {
        OkHttpClient.Builder().readTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(get<Interceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(CryptoApi::class.java)
    }
}