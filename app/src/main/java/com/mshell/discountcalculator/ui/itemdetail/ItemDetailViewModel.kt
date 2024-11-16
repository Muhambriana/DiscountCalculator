package com.mshell.discountcalculator.ui.itemdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.core.data.CalDisRepository
import com.mshell.discountcalculator.core.CalDisEvent
import com.mshell.discountcalculator.core.data.source.CalDisResource
import com.mshell.discountcalculator.databinding.FragmentItemDetailBottomBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ItemDetailViewModel(private val repository: CalDisRepository) : ViewModel() {

    val itemDetail = MutableLiveData<CalDisEvent<CalDisResource<ShoppingItem>>>()

    fun getItemDetail(binding: FragmentItemDetailBottomBinding, shoppingItem: ShoppingItem) {
        itemDetail.postValue(CalDisEvent(CalDisResource.Loading()))
        viewModelScope.launch {
            val result = async { repository.getItemDetail(binding, shoppingItem) }.await()
            result.onSuccess {
                itemDetail.postValue(CalDisEvent(CalDisResource.Success(it)))
            }.onFailure {
                itemDetail.postValue(
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