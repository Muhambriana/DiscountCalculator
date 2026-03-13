package com.mshell.caldis.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mshell.caldis.core.repository.DiscalRepository
import com.mshell.caldis.ui.form.DiscountFormViewModel


class DiscalViewModelFactory(private val discalRepository: DiscalRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DiscountFormViewModel::class.java) -> {
                DiscountFormViewModel(this.discalRepository) as T
            }
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
