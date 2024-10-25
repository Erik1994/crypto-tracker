package com.example.cryptotracker

import android.app.Application
import com.example.cryptotracker.core.data.di.coreDataModule
import com.example.cryptotracker.crypto.data.di.cryptoDataModule
import com.example.cryptotracker.crypto.presentation.di.cryptoPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(modules)
        }
    }
}

private val modules = listOf(
    coreDataModule,
    cryptoDataModule,
    cryptoPresentationModule
)