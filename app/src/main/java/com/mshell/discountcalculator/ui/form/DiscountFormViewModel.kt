package com.mshell.discountcalculator.ui.form

import androidx.lifecycle.LiveData
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

    private val _items = MutableLiveData<DiscalEvent<DiscalResource<MutableList<Form>>>>()
    private val _item = MutableLiveData<DiscalEvent<DiscalResource<Form>>>()
    private val _discountResult = MutableLiveData<DiscalEvent<DiscalResource<List<Form>>>>()

    val items: LiveData<DiscalEvent<DiscalResource<MutableList<Form>>>> get() = _items
    val item: LiveData<DiscalEvent<DiscalResource<Form>>> get() = _item
    val discountResult:  LiveData<DiscalEvent<DiscalResource<List<Form>>>> = _discountResult

    fun getFirstList(count: Int) {
        _items.postValue(DiscalEvent(DiscalResource.Loading()))
        viewModelScope.launch {
            val items = async { repository.getFirstList(count) }.await()
            items.onSuccess {
                if (it.isEmpty()) {
                    _items.postValue(DiscalEvent(DiscalResource.Empty()))
                    return@onSuccess
                }
                _items.postValue(DiscalEvent(DiscalResource.Success(it)))
            }.onFailure {
                _items.postValue(DiscalEvent(DiscalResource.Error(null, it)))
            }

        }
    }

    fun addNewItem(form: Form? = null) {
        _item.postValue(DiscalEvent(DiscalResource.Loading()))
        viewModelScope.launch {
            val item = async { repository.getNewItem(form) }.await()
            item.onSuccess {
                _item.postValue(DiscalEvent(DiscalResource.Success(it)))
            }.onFailure {
                _item.postValue(DiscalEvent(DiscalResource.Error(null, it)))
            }
        }
    }

    fun getResultDiscountPercent(
        list: MutableList<Form>?,
        discountPercent: Double?,
        discountMax: Double?
    ) {
        _discountResult.postValue(DiscalEvent(DiscalResource.Loading()))
        viewModelScope.launch {
            val result = async { repository.getDiscountPercentResult(list, discountPercent, discountMax) }.await()
            result.onSuccess {
                if (it.isNullOrEmpty()) {
                    _discountResult.postValue(DiscalEvent(DiscalResource.Empty()))
                    return@onSuccess
                }
                _discountResult.postValue(DiscalEvent(DiscalResource.Success(it)))
            }.onFailure {
                _discountResult.postValue(DiscalEvent(DiscalResource.Error(null, it)))
            }
        }
    }

    fun getResultDiscountNominal(list: MutableList<Form>?, discountNominal: Double?) {
        _discountResult.postValue(DiscalEvent(DiscalResource.Loading()))
        viewModelScope.launch {
            val result = async { repository.getDiscountNominalResult(list, discountNominal) }.await()
            result.onSuccess {
                if (it.isNullOrEmpty()) {
                    _discountResult.postValue(DiscalEvent(DiscalResource.Empty()))
                    return@onSuccess
                }
                _discountResult.postValue(DiscalEvent(DiscalResource.Success(it)))
            }.onFailure {
                _discountResult.postValue(DiscalEvent(DiscalResource.Error(null, it)))
            }
        }
    }
}