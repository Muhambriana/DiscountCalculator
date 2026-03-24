package com.mshell.discalc.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mshell.discalc.core.data.CalDisRepository
import com.mshell.discalc.ui.shoppinglist.ShoppingItemListViewModel
import com.mshell.discalc.ui.home.HomeViewModel
import com.mshell.discalc.ui.itemdetail.ItemDetailViewModel


class CalDisViewModelFactory(private val calDisRepository: CalDisRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ShoppingItemListViewModel::class.java) -> {
                ShoppingItemListViewModel(this.calDisRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(this.calDisRepository) as T
            }
            modelClass.isAssignableFrom(ItemDetailViewModel::class.java) -> {
                ItemDetailViewModel(this.calDisRepository) as T
            }
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
