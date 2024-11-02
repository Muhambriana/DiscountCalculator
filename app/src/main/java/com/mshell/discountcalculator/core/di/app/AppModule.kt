package com.mshell.discountcalculator.core.di.app


import com.mshell.discountcalculator.core.di.domain.usecase.CalDisInteractor
import com.mshell.discountcalculator.core.di.domain.usecase.CalDisUseCase
import com.mshell.discountcalculator.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val useCaseModule = module {
    factory<CalDisUseCase> { CalDisInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
}