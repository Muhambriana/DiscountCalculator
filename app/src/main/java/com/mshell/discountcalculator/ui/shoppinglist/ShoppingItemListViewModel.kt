package com.mshell.discountcalculator.ui.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.core.data.CalDisRepository
import com.mshell.discountcalculator.core.CalDisEvent
import com.mshell.discountcalculator.core.data.source.CalDisResource
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.utils.config.Config.DEFAULT_DOUBLE_VALUE_ONE
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ShoppingItemListViewModel(private val repository: CalDisRepository) : ViewModel() {

    private val _newItem = MutableLiveData<ShoppingItem>()
    private val _item = MutableLiveData<ShoppingItem>()
    private val _discountResult = MutableLiveData<CalDisEvent<CalDisResource<ShoppingDetail>>>()

    val newItem: LiveData<ShoppingItem> get() = _newItem
    val item: LiveData<ShoppingItem> get() = _item
    val discountResult:  LiveData<CalDisEvent<CalDisResource<ShoppingDetail>>> = _discountResult

    fun addNewItem(shoppingItem: ShoppingItem? = null) {
        viewModelScope.launch {
            val item = async { repository.getNewItem(shoppingItem) }.await()
            _newItem.value = item
        }
    }

    fun increaseItemQuantity(model: ShoppingItem) {
        viewModelScope.launch {
            model.quantity = (model.quantity?.plus(DEFAULT_DOUBLE_VALUE_ONE)) ?: DEFAULT_DOUBLE_VALUE_ONE
            _item.value = model
        }
    }

    fun decreaseItemQuantity(model: ShoppingItem) {
        viewModelScope.launch {
            model.quantity = model.quantity?.minus(DEFAULT_DOUBLE_VALUE_ONE)
            _item.value = model
        }
    }

    fun getResultDiscount(shoppingDetail: ShoppingDetail?) {
        _discountResult.value = CalDisEvent(CalDisResource.Loading())
        viewModelScope.launch {
            val result = async { repository.getDiscountResult(shoppingDetail) }.await()
            result.onSuccess {
                if (it == null) {
                    _discountResult.value = CalDisEvent(CalDisResource.Empty())
                    return@onSuccess
                }
                _discountResult.value = CalDisEvent(CalDisResource.Success(it))
            }.onFailure {
                _discountResult.value = CalDisEvent(CalDisResource.Error(null, it))
            }
        }
    }
}