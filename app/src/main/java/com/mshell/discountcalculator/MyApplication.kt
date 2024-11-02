package com.mshell.discountcalculator

import android.app.Application
import com.mshell.discountcalculator.core.di.app.useCaseModule
import com.mshell.discountcalculator.core.di.app.viewModelModule
import com.mshell.discountcalculator.core.di.core.databaseModule
import com.mshell.discountcalculator.core.di.core.networkModule
import com.mshell.discountcalculator.core.di.core.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}