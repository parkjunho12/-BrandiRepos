package com.junho.brandirepos

import android.app.Application
import com.junho.brandirepos.di.networkModule
import com.junho.brandirepos.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level

class BrandiApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@BrandiApplication)
            androidFileProperties()
            modules(listOf(networkModule, viewModelModule))

        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}