package com.mshell.discountcalculator.ui.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshell.discountcalculator.core.CalDisEvent
import com.mshell.discountcalculator.core.data.source.CalDisResource
import com.mshell.discountcalculator.core.di.domain.usecase.CalDisUseCase
import com.mshell.discountcalculator.core.models.ShoppingDetail
import kotlinx.coroutines.launch

class ShoppingItemListViewModel(private val calDisUseCase: CalDisUseCase) : ViewModel() {

    private val _shoppingDetail = MutableLiveData<CalDisEvent<CalDisResource<ShoppingDetail>>>()

    val shoppingDetail:  LiveData<CalDisEvent<CalDisResource<ShoppingDetail>>> = _shoppingDetail

    fun getShoppingDetail(shoppingId: Long) {
        _shoppingDetail.value = CalDisEvent(CalDisResource.Loading())
        viewModelScope.launch {
            _shoppingDetail.value = calDisUseCase.getShoppingDetailById(shoppingId)
        }
    }
}