package com.mshell.discountcalculator.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mshell.discountcalculator.core.models.Form
import com.mshell.discountcalculator.core.repository.DiscalRepository
import com.mshell.discountcalculator.core.resource.DiscalEvent
import com.mshell.discountcalculator.core.resource.DiscalResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DiscountFormViewModel(private val repository: DiscalRepository): ViewModel() {

    val resourceItemsForm = MutableLiveData<DiscalEvent<DiscalResource<List<Form>>>>()
    val resourceItemForm = MutableLiveData<DiscalEvent<DiscalResource<Form>>>()

    fun getFirstList(count: Int) {
        val items = repository.getFirstList(count)

        resourceItemsForm.postValue(DiscalEvent(DiscalResource.Loading()))
        CoroutineScope(Dispatchers.IO).launch {
            async { items }.await()
            resourceItemsForm.postValue(DiscalEvent(DiscalResource.Success(items)))
        }
    }

    fun getItem() {
        val item = repository.getItem()

        resourceItemForm.postValue(DiscalEvent(DiscalResource.Loading()))
        CoroutineScope(Dispatchers.IO).launch {
            async { item }.await()
            resourceItemForm.postValue(DiscalEvent(DiscalResource.Success(item)))
        }
    }
}