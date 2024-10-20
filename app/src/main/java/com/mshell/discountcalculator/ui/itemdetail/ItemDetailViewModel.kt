package com.mshell.discountcalculator.ui.itemdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshell.discountcalculator.core.models.Form
import com.mshell.discountcalculator.core.data.DiscalRepository
import com.mshell.discountcalculator.core.DiscalEvent
import com.mshell.discountcalculator.core.data.source.DiscalResource
import com.mshell.discountcalculator.databinding.FragmentItemDetailBottomBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ItemDetailViewModel(private val repository: DiscalRepository) : ViewModel() {

    val itemDetail = MutableLiveData<DiscalEvent<DiscalResource<Form>>>()

    fun getItemDetail(binding: FragmentItemDetailBottomBinding) {
        itemDetail.postValue(DiscalEvent(DiscalResource.Loading()))
        viewModelScope.launch {
            val result = async { repository.getItemDetail(binding) }.await()
            result.onSuccess {
                itemDetail.postValue(DiscalEvent(DiscalResource.Success(it)))
            }.onFailure {
                itemDetail.postValue(
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