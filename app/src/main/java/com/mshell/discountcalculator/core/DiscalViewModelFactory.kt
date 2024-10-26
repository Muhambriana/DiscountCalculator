package com.mshell.discountcalculator.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mshell.discountcalculator.core.data.DiscalRepository
import com.mshell.discountcalculator.ui.shoppinglist.ShoppingItemListViewModel
import com.mshell.discountcalculator.ui.home.HomeViewModel
import com.mshell.discountcalculator.ui.itemdetail.ItemDetailViewModel


class DiscalViewModelFactory(private val discalRepository: DiscalRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ShoppingItemListViewModel::class.java) -> {
                ShoppingItemListViewModel(this.discalRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(this.discalRepository) as T
            }
            modelClass.isAssignableFrom(ItemDetailViewModel::class.java) -> {
                ItemDetailViewModel(this.discalRepository) as T
            }
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
