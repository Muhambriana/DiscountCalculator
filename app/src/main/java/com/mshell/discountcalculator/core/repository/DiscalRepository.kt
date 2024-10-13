package com.mshell.discountcalculator.core.repository

import com.mshell.discountcalculator.core.models.DiscountDetail
import com.mshell.discountcalculator.core.models.Form
import com.mshell.discountcalculator.databinding.ActivityHomeBinding
import com.mshell.discountcalculator.databinding.FragmentItemDetailBottomBinding
import com.mshell.discountcalculator.utils.config.Config.DEFAULT_DOUBLE_VALUE
import com.mshell.discountcalculator.utils.config.Config.DEFAULT_DOUBLE_VALUE_ONE
import com.mshell.discountcalculator.utils.config.DiscountType
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DiscalRepository {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        println("Caught exception: $throwable")
    }
    private val dispatchers = (Dispatchers.IO + handler)

    fun addNewItem(form: Form? = null): Result<Form> {
        return Result.success(form ?: Form())
    }

    fun getFirstList(count: Int): Result<MutableList<Form>> {
        return try {
            addNewItem().map { form ->
                mutableListOf<Form>().apply {
                    repeat(count) {
                        add(form)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun getDiscountPercentResult(
        list: MutableList<Form>?,
        discountPercent: Double?,
        discountMax: Double?
    ): Result<MutableList<Form>?> {
        return try {
            Result.success(
                withContext(dispatchers) {
                    var totalAllItem = DEFAULT_DOUBLE_VALUE

                    // Calculate total for all items
                    list?.forEach { form ->
                        form.total = form.itemPrice?.times(form.itemQuantity ?: DEFAULT_DOUBLE_VALUE)
                        totalAllItem += form.total ?: DEFAULT_DOUBLE_VALUE
                    }

                    // Calculate discount
                    val tempDiscount = (discountPercent?.div(100))?.times(totalAllItem) ?: DEFAULT_DOUBLE_VALUE
                    val totalDiscount = minOf(tempDiscount, discountMax ?: DEFAULT_DOUBLE_VALUE)

                    // Apply discount to each item
                    list?.forEach { form ->
                        val proportion = form.total?.div(totalAllItem) ?: DEFAULT_DOUBLE_VALUE
                        val itemDiscount = (proportion * totalDiscount)
                        form.apply {
                            this.itemDiscount = itemDiscount
                            afterDiscount = total?.minus(this.itemDiscount ?: DEFAULT_DOUBLE_VALUE)
                        }
                    }

                    // Return updated list
                    list
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun getDiscountNominalResult(
        list: MutableList<Form>?,
        discountNominal: Double?
    ): Result<MutableList<Form>?> {
        return try {
            withContext(dispatchers) {
                val discount = discountNominal ?: DEFAULT_DOUBLE_VALUE
                val totalQuantity = list?.sumOf { it.itemQuantity ?: DEFAULT_DOUBLE_VALUE } ?: DEFAULT_DOUBLE_VALUE
                val discountPerItem = discountNominal?.div(totalQuantity)
                val sumAllItems = list?.sumOf { form ->
                    (form.itemPrice ?: DEFAULT_DOUBLE_VALUE) * (form.itemQuantity ?: DEFAULT_DOUBLE_VALUE)
                } ?: DEFAULT_DOUBLE_VALUE

                list?.forEach { form ->
                    form.apply {
                        total = itemPrice?.times(itemQuantity ?: DEFAULT_DOUBLE_VALUE)
                        itemDiscount = if (sumAllItems < discount) {
                            total
                        } else {
                            discountPerItem?.times(itemQuantity ?: DEFAULT_DOUBLE_VALUE)
                        }
                        afterDiscount = total?.let { totalValue ->
                            totalValue - (itemDiscount ?: DEFAULT_DOUBLE_VALUE)
                        }?.takeIf { it > DEFAULT_DOUBLE_VALUE } ?: DEFAULT_DOUBLE_VALUE
                    }
                }

                Result.success(list)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }



    fun getDiscountDetail(binding: ActivityHomeBinding): Result<DiscountDetail?> {
        return try {
            Result.success(
                DiscountDetail().apply {
                    additional = binding.edAdditional.edCurrency.getCleanText().toDouble()

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
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getItemDetail(binding: FragmentItemDetailBottomBinding): Result<Form?> {
        return try {
            Result.success(
                Form().apply {
                    itemName = binding.edItemName.text.toString()
                    itemPrice = binding.edItemPrice.edCurrency.getCleanText().toDouble()
                    itemQuantity = DEFAULT_DOUBLE_VALUE_ONE
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}