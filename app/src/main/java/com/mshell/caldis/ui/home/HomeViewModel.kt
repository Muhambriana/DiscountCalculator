package com.mshell.caldis.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshell.caldis.core.data.CalDisRepository
import com.mshell.caldis.core.CalDisEvent
import com.mshell.caldis.core.data.source.CalDisResource
import com.mshell.caldis.core.models.ShoppingDetail
import com.mshell.caldis.databinding.ActivityHomeBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: CalDisRepository) : ViewModel() {

    val shoppingDetail = MutableLiveData<CalDisEvent<CalDisResource<ShoppingDetail>>>()

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