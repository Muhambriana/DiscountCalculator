package com.mshell.discountcalculator.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mshell.discountcalculator.core.CalDisEvent
import com.mshell.discountcalculator.core.data.source.CalDisResource
import com.mshell.discountcalculator.core.di.domain.usecase.CalDisUseCase
import com.mshell.discountcalculator.core.models.DiscountDetail
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.databinding.ActivityHomeBinding
import com.mshell.discountcalculator.utils.config.DiscountType
import com.mshell.discountcalculator.utils.config.ExceptionTypeEnum
import kotlinx.coroutines.launch

class HomeViewModel(private val calDisUseCase: CalDisUseCase) : ViewModel() {

    private var _shoppingId = MutableLiveData<CalDisEvent<CalDisResource<Long>>>()

    val shoppingId: LiveData<CalDisEvent<CalDisResource<Long>>> = _shoppingId
    val listItem = calDisUseCase.getAllShoppingItem().asLiveData()

    fun insertShoppingDetail(binding: ActivityHomeBinding) {
        _shoppingId.value = CalDisEvent(CalDisResource.Loading())
        viewModelScope.launch {
            try {
                val discountDetail = DiscountDetail().apply {
                    when (binding.radioGroupDiscount.checkedRadioButtonId) {
                        binding.rbPercent.id -> {
                            discountType = DiscountType.PERCENT
                            discountPercent = binding.layoutFormDiscountPercent
                                .edPercent
                                .text
                                ?.toString()
                                ?.toInt()
                            discountMax = binding.layoutFormDiscountPercent
                                .edMaxDiscount
                                .edCurrency
                                .getCleanText()
                                .toDouble()
                        }

                        binding.rbNominal.id -> {
                            discountType = DiscountType.NOMINAL
                            discountNominal = binding.layoutFormDiscountNominal
                                .edDiscount
                                .edCurrency
                                .getCleanText()
                                .toDouble()
                        }
                    }

                }

                val shoppingDetail = ShoppingDetail(
                    discountDetail = discountDetail,
                    additional = binding.edAdditional.edCurrency.getCleanText().toDouble()
                )
                _shoppingId.value = calDisUseCase.insertShoppingDetail(shoppingDetail)
            } catch (e: Exception) {
                _shoppingId.value = CalDisEvent(CalDisResource.Error(null, e, ExceptionTypeEnum.RESULT_ERROR))
            }
        }
    }

}