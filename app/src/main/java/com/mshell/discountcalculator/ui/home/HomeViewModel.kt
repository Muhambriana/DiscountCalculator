package com.mshell.discountcalculator.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshell.discountcalculator.core.data.DiscalRepository
import com.mshell.discountcalculator.core.DiscalEvent
import com.mshell.discountcalculator.core.data.source.DiscalResource
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.databinding.ActivityHomeBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: DiscalRepository) : ViewModel() {

    val shoppingDetail = MutableLiveData<DiscalEvent<DiscalResource<ShoppingDetail>>>()

    fun getShoppingDetail(binding: ActivityHomeBinding) {
        shoppingDetail.postValue(DiscalEvent(DiscalResource.Loading()))
        viewModelScope.launch {
            val result = async { repository.getShoppingDetail(binding) }.await()
            result.onSuccess {
                shoppingDetail.postValue(DiscalEvent(DiscalResource.Success(it)))
            }.onFailure {
                shoppingDetail.postValue(
                    DiscalEvent(
                        DiscalResource.Error(
                            null, it
                        )
                    )
                )
            }
        }
    }
}