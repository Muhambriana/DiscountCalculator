package com.mshell.discountcalculator.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshell.discountcalculator.core.models.DiscountDetail
import com.mshell.discountcalculator.core.data.DiscalRepository
import com.mshell.discountcalculator.core.DiscalEvent
import com.mshell.discountcalculator.core.data.source.DiscalResource
import com.mshell.discountcalculator.databinding.ActivityHomeBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: DiscalRepository) : ViewModel() {

    val discountDetail = MutableLiveData<DiscalEvent<DiscalResource<DiscountDetail>>>()

    fun getDiscountDetail(binding: ActivityHomeBinding) {
        discountDetail.postValue(DiscalEvent(DiscalResource.Loading()))
        viewModelScope.launch {
            val result = async { repository.getDiscountDetail(binding) }.await()
            result.onSuccess {
                discountDetail.postValue(DiscalEvent(DiscalResource.Success(it)))
            }.onFailure {
                discountDetail.postValue(
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