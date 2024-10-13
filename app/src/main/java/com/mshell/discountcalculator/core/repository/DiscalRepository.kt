package com.mshell.discountcalculator.core.repository

import com.mshell.discountcalculator.core.models.DiscountDetail
import com.mshell.discountcalculator.core.models.Form
import com.mshell.discountcalculator.databinding.ActivityHomeBinding
import com.mshell.discountcalculator.databinding.FragmentItemDetailBottomBinding
import com.mshell.discountcalculator.utils.config.DiscountType
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class DiscalRepository {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        println("Caught exception: $throwable")
    }
    private val dispatchers = (Dispatchers.IO + handler)

    fun addNewItem(form: Form? = null): Form {
        if (form == null) return Form()
        return form
    }

    fun getFirstList(count: Int): MutableList<Form> {
        val list = mutableListOf<Form>()
        for (i in 1..count) {
            list.add(addNewItem())
        }
        return list
    }

    suspend fun getDiscountPercentResult(
        list: MutableList<Form>?,
        discountPercent: Double?,
        discountMax: Double?
    ): MutableList<Form>? {
        val result = withContext(dispatchers) {
            var totalAllItem = 0.0
            async {
                list?.forEach {
                    it.total = it.itemPrice?.times(it.itemQuantity ?: 0.0)
                    totalAllItem += it.total ?: 0.0
                }
            }.await()
            val tempDiscount = ((discountPercent?.div(100))?.times(totalAllItem))
            val totalDiscount = minOf(tempDiscount ?: 0.0, discountMax ?: 0.0)

            async {
                list?.forEach {
                    val firstCalculation = it.total?.div(totalAllItem)
                    val secondCalculation = (firstCalculation)?.times(100)
                    it.discount = (secondCalculation?.times(totalDiscount))?.div(100)
                }
            }.await()

            list
        }
        return result
    }

    suspend fun getDiscountNominalResult(
        list: MutableList<Form>?,
        discountNominal: Double?
    ): MutableList<Form>? {
        val discount = discountNominal ?: 0.0
        val result = withContext(dispatchers) {
            val sumAllItem = list?.sumOf {
                val price = it.itemPrice ?: 0.0
                val quantity = it.itemQuantity ?: 0.0
                val total = price * quantity
                total
            } ?: 0.0
            val discountPerItem = discountNominal?.div(list?.sumOf { it.itemQuantity ?: 0.0} ?: 0.0)
            async {
                list?.forEach {
                    it.total = it.itemPrice?.times(it.itemQuantity ?: 0.0)
                    it.discount =
                        if (sumAllItem < discount) it.total
                        else discountPerItem?.times(it.itemQuantity ?: 0.0)
                }
            }.await()
            list
        }
        return result
    }

    fun getDiscountDetail(binding: ActivityHomeBinding): Result<DiscountDetail?> {
        try {
            return Result.success(
                DiscountDetail().apply {
                    additional = binding.edAdditional.getCleanText().toDouble()
                    when (binding.radioGroupDiscount.checkedRadioButtonId) {
                        binding.rbPercent.id -> {
                            discountType = DiscountType.PERCENT
                            discountPercent = binding
                                .layoutFormDiscountPercent
                                .edDiscountPercent
                                .text
                                ?.toString()
                                ?.toInt()
                            discountMax = binding
                                .layoutFormDiscountPercent
                                .edMaxDiscount
                                .getCleanText()
                                .toDouble()
                        }

                        binding.rbNominal.id -> {
                            discountType = DiscountType.NOMINAL
                            discountNominal = binding
                                .layoutFormDiscountNominal
                                .edDiscount
                                .getCleanText()
                                .toDouble()
                        }
                    }
                }
            )
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun getItemDetail(binding: FragmentItemDetailBottomBinding): Result<Form?> {
        try {
            val itemDetail = Form()
            itemDetail.apply {
                itemName = binding.edItemName.text.toString()
                itemPrice = binding.edItemPrice.getCleanText().toDouble()
                itemQuantity = 1.0
            }
            return Result.success(itemDetail)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

}