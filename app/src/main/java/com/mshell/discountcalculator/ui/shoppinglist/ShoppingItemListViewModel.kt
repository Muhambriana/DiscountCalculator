package com.mshell.discountcalculator.ui.shoppinglist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.core.data.DiscalRepository
import com.mshell.discountcalculator.core.DiscalEvent
import com.mshell.discountcalculator.core.data.source.DiscalResource
import com.mshell.discountcalculator.utils.config.Config.DEFAULT_DOUBLE_VALUE_ONE
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ShoppingItemListViewModel(private val repository: DiscalRepository) : ViewModel() {

    private val _newItem = MutableLiveData<ShoppingItem>()
    private val _item = MutableLiveData<ShoppingItem>()
    private val _discountResult = MutableLiveData<DiscalEvent<DiscalResource<List<ShoppingItem>>>>()

    val newItem: LiveData<ShoppingItem> get() = _newItem
    val item: LiveData<ShoppingItem> get() = _item
    val discountResult:  LiveData<DiscalEvent<DiscalResource<List<ShoppingItem>>>> = _discountResult

    fun addNewItem(shoppingItem: ShoppingItem? = null) {
        viewModelScope.launch {
            val item = async { repository.getNewItem(shoppingItem) }.await()
            _newItem.value = item
        }
    }

    fun increaseItemQuantity(model: ShoppingItem) {
        viewModelScope.launch {
            model.itemQuantity = (model.itemQuantity?.plus(DEFAULT_DOUBLE_VALUE_ONE)) ?: DEFAULT_DOUBLE_VALUE_ONE
            _item.value = model
        }
    }

    fun decreaseItemQuantity(model: ShoppingItem) {
        viewModelScope.launch {
            model.itemQuantity = model.itemQuantity?.minus(DEFAULT_DOUBLE_VALUE_ONE)
            _item.value = model
        }
    }

    fun getResultDiscountPercent(
        list: MutableList<ShoppingItem>?,
        discountPercent: Double?,
        discountMax: Double?
    ) {
        _discountResult.value = DiscalEvent(DiscalResource.Loading())
        viewModelScope.launch {
            val result = async { repository.getDiscountPercentResult(list, discountPercent, discountMax) }.await()
            result.onSuccess {
                if (it.isNullOrEmpty()) {
                    _discountResult.value = DiscalEvent(DiscalResource.Empty())
                    return@onSuccess
                }
                _discountResult.value = DiscalEvent(DiscalResource.Success(it))
            }.onFailure {
                _discountResult.value = DiscalEvent(DiscalResource.Error(null, it))
            }
        }
    }

    fun getResultDiscountNominal(list: MutableList<ShoppingItem>?, discountNominal: Double?) {
        _discountResult.value = DiscalEvent(DiscalResource.Loading())
        viewModelScope.launch {
            val result = async { repository.getDiscountNominalResult(list, discountNominal) }.await()
            result.onSuccess {
                if (it.isNullOrEmpty()) {
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