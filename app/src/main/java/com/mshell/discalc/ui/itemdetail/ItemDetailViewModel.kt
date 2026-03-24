package com.mshell.discalc.ui.itemdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshell.discalc.core.models.ShoppingItem
import com.mshell.discalc.core.data.CalDisRepository
import com.mshell.discalc.core.CalDisEvent
import com.mshell.discalc.core.data.source.CalDisResource
import com.mshell.discalc.databinding.FragmentItemDetailBottomBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ItemDetailViewModel(private val repository: CalDisRepository) : ViewModel() {

    val itemDetail = MutableLiveData<CalDisEvent<CalDisResource<ShoppingItem>>>()

    fun getItemDetail(binding: FragmentItemDetailBottomBinding) {
        itemDetail.postValue(CalDisEvent(CalDisResource.Loading()))
        viewModelScope.launch {
            val result = async { repository.getItemDetail(binding) }.await()
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