package com.mshell.discountcalculator.ui.form

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshell.discountcalculator.core.models.Form
import com.mshell.discountcalculator.core.repository.DiscalRepository
import com.mshell.discountcalculator.core.resource.DiscalEvent
import com.mshell.discountcalculator.core.resource.DiscalResource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DiscountFormViewModel(private val repository: DiscalRepository): ViewModel() {

    val resourceItemsForm = MutableLiveData<DiscalEvent<DiscalResource<List<Form>>>>()
    val resourceItemForm = MutableLiveData<DiscalEvent<DiscalResource<Form>>>()
    val discountResult = MutableLiveData<DiscalEvent<DiscalResource<List<Form>>>>()

    fun getFirstList(count: Int) {
        resourceItemsForm.postValue(DiscalEvent(DiscalResource.Loading()))

        viewModelScope.launch {
            val items = repository.getFirstList(count)
            async { items }.await()
            resourceItemsForm.postValue(DiscalEvent(DiscalResource.Success(items)))
        }
    }

    fun addNewItem(form: Form? = null) {
        resourceItemForm.postValue(DiscalEvent(DiscalResource.Loading()))

        viewModelScope.launch {
            val item = repository.addNewItem(form)
            async { item }.await()
            resourceItemForm.postValue(DiscalEvent(DiscalResource.Success(item)))
        }
    }

    fun getResultDiscountPercent(list: MutableList<Form>?, discountPercent: Double?, discountMax: Double?) {
        discountResult.postValue(DiscalEvent(DiscalResource.Loading()))

        viewModelScope.launch {
            val result = repository.getDiscountPercentResult(list, discountPercent, discountMax)
            async { result }.await()
            discountResult.postValue(DiscalEvent(DiscalResource.Success(result)))
        }
    }

    fun getResultDiscountNominal(list: MutableList<Form>?, discountNominal: Double?) {
        discountResult.postValue(DiscalEvent(DiscalResource.Loading()))

        viewModelScope.launch {
            val result = repository.getDiscountNominalResult(list, discountNominal)
            async { result }.await()
            discountResult.postValue(DiscalEvent(DiscalResource.Success(result)))
        }
    }
}