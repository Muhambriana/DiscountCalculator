package com.mshell.discountcalculator.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mshell.discountcalculator.core.repository.DiscalRepository
import com.mshell.discountcalculator.ui.form.DiscountFormViewModel
import com.mshell.discountcalculator.ui.home.HomeViewModel


class DiscalViewModelFactory(private val discalRepository: DiscalRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DiscountFormViewModel::class.java) -> {
                DiscountFormViewModel(this.discalRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(this.discalRepository) as T
            }
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
