package com.herargos.herargosadmistrativo

import android.app.Application
import com.herargos.herargosadmistrativo.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@MainApp)
        }
    }
}