package com.mshell.discountcalculator.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mshell.discountcalculator.core.data.CalDisRepository
import com.mshell.discountcalculator.ui.shoppinglist.ShoppingItemListViewModel
import com.mshell.discountcalculator.ui.home.HomeViewModel
import com.mshell.discountcalculator.ui.itemdetail.ItemDetailViewModel


class CalDisViewModelFactory(private val calDisRepository: CalDisRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ItemDetailViewModel::class.java) -> {
                ItemDetailViewModel(this.calDisRepository) as T
            }
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
