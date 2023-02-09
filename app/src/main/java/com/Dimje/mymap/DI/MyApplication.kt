package com.Dimje.mymap.DI

import android.app.Application
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            modules(appModule)
        }
    }
}