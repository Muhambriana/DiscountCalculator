package com.mshell.discountcalculator.core.di.core

import androidx.room.Room
import com.mshell.discountcalculator.BuildConfig
import com.mshell.discountcalculator.core.data.CalDisRepository2
import com.mshell.discountcalculator.core.data.source.local.LocalDataSource
import com.mshell.discountcalculator.core.data.source.local.room.CalDisDatabase
import com.mshell.discountcalculator.core.di.domain.repository.InterfaceCalDisRepository
import com.mshell.discountcalculator.utils.AppExecutors
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    factory { get<CalDisDatabase>().calDisDao() }
    single {
        val passphrase: ByteArray? = if (BuildConfig.ENABLE_DATABASE_ENCRYPTION) {
            SQLiteDatabase.getBytes(BuildConfig.DATABASE_KEY.toCharArray())
        } else null
        val factory = passphrase?.let { SupportFactory(it) }

        Room.databaseBuilder(
            androidContext(),
            CalDisDatabase::class.java, "Caldis.db"
        ).apply {
            if (factory != null) openHelperFactory(factory)
        }.fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {

    }
    single {

    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    factory { AppExecutors() }
    single<InterfaceCalDisRepository> {
        CalDisRepository2(
            get(),
            get()
        )
    }
}