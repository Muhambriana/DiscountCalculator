package com.mshell.discountcalculator.ui.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.mshell.discountcalculator.core.data.source.CalDisResource
import com.mshell.discountcalculator.core.di.domain.usecase.CalDisUseCase
import com.mshell.discountcalculator.core.models.ShoppingDetail

class ShoppingItemListViewModel(private val calDisUseCase: CalDisUseCase) : ViewModel() {

    fun getShoppingDetail(shoppingId: Long): LiveData<CalDisResource<ShoppingDetail>> =
        calDisUseCase.getShoppingDetailById(shoppingId).asLiveData()

}