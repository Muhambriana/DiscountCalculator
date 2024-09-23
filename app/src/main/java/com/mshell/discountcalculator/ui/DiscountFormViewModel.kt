package com.mshell.discountcalculator.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mshell.discountcalculator.core.models.Form
import com.mshell.discountcalculator.core.repository.DiscalRepository
import com.mshell.discountcalculator.core.resource.DiscalEvent
import com.mshell.discountcalculator.core.resource.DiscalResource

class DiscountFormViewModel(private val repository: DiscalRepository): ViewModel() {

    val resourceItemsForm = MutableLiveData<DiscalEvent<DiscalResource<List<Form>>>>()
    val resourceItemForm = MutableLiveData<DiscalEvent<DiscalResource<Form>>>()

    fun getFirstList(count: Int) {
        val items = repository.getFirstList(count)

        resourceItemsForm.postValue(DiscalEvent(DiscalResource.Loading()))
        resourceItemsForm.postValue(DiscalEvent(DiscalResource.Success(items)))
    }

    fun getItem() {
        val item = repository.getItem()

        resourceItemForm.postValue(DiscalEvent(DiscalResource.Loading()))
        resourceItemForm.postValue(DiscalEvent(DiscalResource.Success(item)))
    }
}