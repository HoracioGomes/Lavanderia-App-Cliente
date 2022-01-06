package com.example.lavanderia_cliente

import android.app.Application
import com.example.lavanderia_cliente.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class LavanderiaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@LavanderiaApplication)
            modules(appModule)
        }
    }
}