package com.mshell.discountcalculator.ui.form

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mshell.discountcalculator.core.models.Form
import com.mshell.discountcalculator.core.repository.DiscalRepository
import com.mshell.discountcalculator.core.resource.DiscalEvent
import com.mshell.discountcalculator.core.resource.DiscalResource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DiscountFormViewModel(private val repository: DiscalRepository): ViewModel() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        println("Caught exception: $throwable")
    }
    private val dispatchers = (Dispatchers.IO + handler)

    val resourceItemsForm = MutableLiveData<DiscalEvent<DiscalResource<List<Form>>>>()
    val resourceItemForm = MutableLiveData<DiscalEvent<DiscalResource<Form>>>()
    val discountResult = MutableLiveData<DiscalEvent<DiscalResource<List<Form>>>>()

    fun getFirstList(count: Int) {
        resourceItemsForm.postValue(DiscalEvent(DiscalResource.Loading()))

        CoroutineScope(dispatchers).launch {
            val items = repository.getFirstList(count)
            async { items }.await()
            resourceItemsForm.postValue(DiscalEvent(DiscalResource.Success(items)))
        }
    }

    fun getItem() {
        resourceItemForm.postValue(DiscalEvent(DiscalResource.Loading()))

        CoroutineScope(dispatchers).launch {
            val item = repository.getItem()
            async { item }.await()
            resourceItemForm.postValue(DiscalEvent(DiscalResource.Success(item)))
        }
    }

    fun getResultDiscountPercent(list: MutableList<Form>?, discountPercent: Double?, discountMax: Double?) {
        discountResult.postValue(DiscalEvent(DiscalResource.Loading()))

        CoroutineScope(dispatchers).launch {
            val result = repository.getDiscountPercentResult(list, discountPercent, discountMax)
            async { result }.await()
            discountResult.postValue(DiscalEvent(DiscalResource.Success(result)))
        }
    }

    fun getResultDiscountNominal(list: MutableList<Form>?, discountNominal: Double?) {
        discountResult.postValue(DiscalEvent(DiscalResource.Loading()))

        CoroutineScope(dispatchers).launch {
            val result = repository.getDiscountNominalResult(list, discountNominal)
            async { result }.await()
            discountResult.postValue(DiscalEvent(DiscalResource.Success(result)))
        }
    }
}