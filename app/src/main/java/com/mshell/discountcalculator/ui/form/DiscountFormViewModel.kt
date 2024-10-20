package com.mshell.discountcalculator.ui.form

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshell.discountcalculator.core.models.Form
import com.mshell.discountcalculator.core.data.DiscalRepository
import com.mshell.discountcalculator.core.DiscalEvent
import com.mshell.discountcalculator.core.data.source.DiscalResource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DiscountFormViewModel(private val repository: DiscalRepository) : ViewModel() {

    val resourceItemsForm = MutableLiveData<DiscalEvent<DiscalResource<List<Form>>>>()
    val resourceItemForm = MutableLiveData<DiscalEvent<DiscalResource<Form>>>()
    val discountResult = MutableLiveData<DiscalEvent<DiscalResource<List<Form>>>>()

    fun getFirstList(count: Int) {
        resourceItemsForm.postValue(DiscalEvent(DiscalResource.Loading()))
        viewModelScope.launch {
            val items = async { repository.getFirstList(count) }.await()
            items.onSuccess {
                if (it.isEmpty()) {
                    resourceItemsForm.postValue(DiscalEvent(DiscalResource.Empty()))
                    return@onSuccess
                }
                resourceItemsForm.postValue(DiscalEvent(DiscalResource.Success(it)))
            }.onFailure {
                resourceItemsForm.postValue(DiscalEvent(DiscalResource.Error(null, it)))
            }

        }
    }

    fun addNewItem(form: Form? = null) {
        resourceItemForm.postValue(DiscalEvent(DiscalResource.Loading()))
        viewModelScope.launch {
            val item = async { repository.getNewItem(form) }.await()
            item.onSuccess {
                resourceItemForm.postValue(DiscalEvent(DiscalResource.Success(it)))
            }.onFailure {
                resourceItemForm.postValue(DiscalEvent(DiscalResource.Error(null, it)))
            }
        }
    }

    fun getResultDiscountPercent(
        list: MutableList<Form>?,
        discountPercent: Double?,
        discountMax: Double?
    ) {
        discountResult.postValue(DiscalEvent(DiscalResource.Loading()))
        viewModelScope.launch {
            val result = async { repository.getDiscountPercentResult(list, discountPercent, discountMax) }.await()
            result.onSuccess {
                if (it.isNullOrEmpty()) {
                    discountResult.postValue(DiscalEvent(DiscalResource.Empty()))
                    return@onSuccess
                }
                discountResult.postValue(DiscalEvent(DiscalResource.Success(it)))
            }.onFailure {
                discountResult.postValue(DiscalEvent(DiscalResource.Error(null, it)))
            }
        }
    }

    fun getResultDiscountNominal(list: MutableList<Form>?, discountNominal: Double?) {
        discountResult.postValue(DiscalEvent(DiscalResource.Loading()))
        viewModelScope.launch {
            val result = async { repository.getDiscountNominalResult(list, discountNominal) }.await()
            result.onSuccess {
                if (it.isNullOrEmpty()) {
                    discountResult.postValue(DiscalEvent(DiscalResource.Empty()))
                    return@onSuccess
                }
                discountResult.postValue(DiscalEvent(DiscalResource.Success(it)))
            }.onFailure {
                discountResult.postValue(DiscalEvent(DiscalResource.Error(null, it)))
            }
        }
    }
}