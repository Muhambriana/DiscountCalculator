package com.mshell.discountcalculator.ui.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.core.data.DiscalRepository
import com.mshell.discountcalculator.core.DiscalEvent
import com.mshell.discountcalculator.core.data.source.DiscalResource
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.utils.config.Config.DEFAULT_DOUBLE_VALUE_ONE
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ShoppingItemListViewModel(private val repository: DiscalRepository) : ViewModel() {

    private val _newItem = MutableLiveData<ShoppingItem>()
    private val _item = MutableLiveData<ShoppingItem>()
    private val _discountResult = MutableLiveData<DiscalEvent<DiscalResource<ShoppingDetail>>>()

    val newItem: LiveData<ShoppingItem> get() = _newItem
    val item: LiveData<ShoppingItem> get() = _item
    val discountResult:  LiveData<DiscalEvent<DiscalResource<ShoppingDetail>>> = _discountResult

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
        _discountResult.value = DiscalEvent(DiscalResource.Loading())
        viewModelScope.launch {
            val result = async { repository.getDiscountResult(shoppingDetail) }.await()
            result.onSuccess {
                if (it == null) {
                    _discountResult.value = DiscalEvent(DiscalResource.Empty())
                    return@onSuccess
                }
                _discountResult.value = DiscalEvent(DiscalResource.Success(it))
            }.onFailure {
                _discountResult.value = DiscalEvent(DiscalResource.Error(null, it))
            }
        }
    }
}