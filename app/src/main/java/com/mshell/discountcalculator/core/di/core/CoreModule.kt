package com.mshell.discountcalculator.core.di.core

import androidx.room.Room
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
        val passphrase: ByteArray = SQLiteDatabase.getBytes("garuda".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            CalDisDatabase::class.java, "Caldis.db"
        ).fallbackToDestructiveMigration().openHelperFactory(factory).build()
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