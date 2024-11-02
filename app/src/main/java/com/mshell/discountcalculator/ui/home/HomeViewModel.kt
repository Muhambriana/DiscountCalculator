package com.mshell.discountcalculator.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mshell.discountcalculator.core.data.CalDisRepository
import com.mshell.discountcalculator.core.CalDisEvent
import com.mshell.discountcalculator.core.data.source.CalDisResource
import com.mshell.discountcalculator.core.di.domain.usecase.CalDisUseCase
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.databinding.ActivityHomeBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel(private val calDisUseCase: CalDisUseCase) : ViewModel() {

    val shoppingDetail = MutableLiveData<CalDisEvent<CalDisResource<ShoppingDetail>>>()
    val list = calDisUseCase.getAllShoppingItem().asLiveData()

    suspend fun insertShoppingItemList(list: List<ShoppingItem>) {
        calDisUseCase.insertListShoppingItem(list)
    }
    fun getShoppingDetail(binding: ActivityHomeBinding) {
        shoppingDetail.postValue(CalDisEvent(CalDisResource.Loading()))
        viewModelScope.launch {
            val result = async { repository.getShoppingDetail(binding) }.await()
            result.onSuccess {
                shoppingDetail.postValue(CalDisEvent(CalDisResource.Success(it)))
            }.onFailure {
                shoppingDetail.postValue(
                    CalDisEvent(
                        CalDisResource.Error(
                            null, it
                        )
                    )
                )
            }
        }
    }
}