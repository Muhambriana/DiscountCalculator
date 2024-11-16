package com.mshell.discountcalculator.core.data.source.local

import com.mshell.discountcalculator.core.models.DiscountDetail
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.databinding.ActivityHomeBinding
import com.mshell.discountcalculator.databinding.FragmentItemDetailBottomBinding
import com.mshell.discountcalculator.utils.config.Config.DEFAULT_DOUBLE_VALUE
import com.mshell.discountcalculator.utils.config.DiscountType
import com.mshell.discountcalculator.utils.helper.Helper.takeZeroIfNegative

class CalDisDataSource {

    fun createNewItem(shoppingItem: ShoppingItem? = null): ShoppingItem {
        val item = shoppingItem ?: ShoppingItem()
        return item.apply {
            totalPrice = quantity.times(pricePerUnit)
        }
    }

    fun calculateShoppingDetail(shoppingDetail: ShoppingDetail): Result<ShoppingDetail?> {
        try {
            shoppingDetail.apply {
                totalQuantity = listItem.sumOf { it.quantity }
                totalShopping = listItem.sumOf { it.totalPrice }
                total = totalShopping.plus(additional)
            }
            shoppingDetail.discountDetail.let {
                when (it.discountType) {
                    DiscountType.NOMINAL -> {
                        return calculateDiscountNominal(shoppingDetail, it.discountNominal ?: DEFAULT_DOUBLE_VALUE)
                    }

                    DiscountType.PERCENT -> {
                        return calculateDiscountPercent(
                            shoppingDetail,
                            it.discountPercent?.toDouble(),
                            it.discountMax
                        )
                    }

                    else -> {
                        return Result.success(null)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    private fun calculateDiscountNominal(
        shoppingDetail: ShoppingDetail,
        discountNominal: Double
    ): Result<ShoppingDetail?> {
        return try {
            val totalQuantity = shoppingDetail.totalQuantity
            val discountPerUnit = discountNominal.div(totalQuantity)
            val sumAllItems = shoppingDetail.totalShopping
            shoppingDetail.listItem.forEach { shoppingItem ->
                shoppingItem.apply {
                    totalDiscount = if (sumAllItems < discountNominal) {
                        totalPrice
                    } else {
                        discountPerUnit.times(quantity)
                    }
                    this.discountPerUnit = totalDiscount.div(quantity)
                    this.pricePerUnitAfterDiscount = takeZeroIfNegative(
                        this.pricePerUnit.minus(this.discountPerUnit)
                    )
                    totalPriceAfterDiscount = takeZeroIfNegative(
                        totalPrice.minus(totalDiscount)
                    )
                }
            }
            shoppingDetail.apply {
                this.discount = listItem.sumOf { it.totalDiscount }
                totalAfterDiscount = total.minus(this.discount)
            }

            Result.success(shoppingDetail)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    private fun calculateDiscountPercent(
        shoppingDetail: ShoppingDetail?,
        discountPercent: Double?,
        discountMax: Double?
    ): Result<ShoppingDetail?> {
        return try {
            val totalAllItem = shoppingDetail?.totalShopping ?: DEFAULT_DOUBLE_VALUE
            // Calculate discount
            val tempDiscount =
                (discountPercent?.div(100))?.times(totalAllItem) ?: DEFAULT_DOUBLE_VALUE
            val totalDiscount = minOf(tempDiscount, discountMax ?: DEFAULT_DOUBLE_VALUE)

            // Apply discount to each item
            shoppingDetail?.listItem?.forEach { shoppingItem ->
                val proportion = shoppingItem.totalPrice.div(totalAllItem)
                val itemDiscount = (proportion * totalDiscount)
                shoppingItem.apply {
                    this.totalDiscount = itemDiscount
                    this.discountPerUnit = this.totalDiscount.div(quantity)
                    this.pricePerUnitAfterDiscount = takeZeroIfNegative(
                        this.pricePerUnit.minus(this.discountPerUnit)
                    )
                    totalPriceAfterDiscount = takeZeroIfNegative(
                        totalPrice.minus(this.totalDiscount)
                    )
                }
            }
            shoppingDetail?.apply {
                discount = listItem.sumOf { it.totalDiscount }
                totalAfterDiscount = total.minus(discount)
            }
            // Return updated list
            Result.success(shoppingDetail)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    fun getShoppingDetail(binding: ActivityHomeBinding): Result<ShoppingDetail?> {
        return try {
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
            Result.success(
                ShoppingDetail(
                    discountDetail = discountDetail,
                    additional = binding.edAdditional.edCurrency.getCleanText().toDouble()
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getItemDetail(binding: FragmentItemDetailBottomBinding, shoppingItem: ShoppingItem): Result<ShoppingItem?> {
        return try {
            Result.success(
                shoppingItem.apply {
                    itemName = binding.edItemName.text.toString()
                    pricePerUnit = binding.edPricePerUnit.edCurrency.getCleanText().toDouble()
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}