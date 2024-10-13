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
                        val proportion = form.total?.div(totalAllItem) ?: 0.0
                        val itemDiscount = (proportion * totalDiscount)
                        form.discount = itemDiscount
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
            Result.success(
                withContext(dispatchers) {
                    val discount = discountNominal ?: DEFAULT_DOUBLE_VALUE
                    // Calculate the sum of all items' totals
                    val sumAllItem = list?.sumOf {
                        val price = it.itemPrice ?: DEFAULT_DOUBLE_VALUE
                        val quantity = it.itemQuantity ?: DEFAULT_DOUBLE_VALUE
                        price * quantity
                    } ?: DEFAULT_DOUBLE_VALUE

                    // Calculate discount per item based on quantity
                    val totalQuantity = list?.sumOf { it.itemQuantity ?: DEFAULT_DOUBLE_VALUE } ?: DEFAULT_DOUBLE_VALUE
                    val discountPerItem = discountNominal?.div(totalQuantity)

                    // Apply discount to each item
                    list?.forEach { form ->
                        form.total = form.itemPrice?.times(form.itemQuantity ?: DEFAULT_DOUBLE_VALUE)
                        form.discount = if (sumAllItem < discount) {
                            form.total // Full discount
                        } else {
                            discountPerItem?.times(form.itemQuantity ?: DEFAULT_DOUBLE_VALUE)
                        }
                    }
                    list // Return updated list
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }


    fun getDiscountDetail(binding: ActivityHomeBinding): Result<DiscountDetail?> {
        return try {
            Result.success(
                DiscountDetail().apply {
                    additional = binding.edAdditional.getCleanText().toDouble()

                    when (binding.radioGroupDiscount.checkedRadioButtonId) {
                        binding.rbPercent.id -> {
                            discountType = DiscountType.PERCENT
                            discountPercent = binding.layoutFormDiscountPercent
                                .edDiscountPercent
                                .text
                                ?.toString()
                                ?.toInt()
                            discountMax = binding.layoutFormDiscountPercent
                                .edMaxDiscount
                                .getCleanText()
                                .toDouble()
                        }
                        binding.rbNominal.id -> {
                            discountType = DiscountType.NOMINAL
                            discountNominal = binding.layoutFormDiscountNominal
                                .edDiscount
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
                    itemPrice = binding.edItemPrice.getCleanText().toDouble()
                    itemQuantity = DEFAULT_DOUBLE_VALUE_ONE
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}