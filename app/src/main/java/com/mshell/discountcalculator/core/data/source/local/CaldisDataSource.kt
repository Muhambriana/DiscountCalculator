package com.mshell.discountcalculator.core.data.source.local

import com.mshell.discountcalculator.core.models.DiscountDetail
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.databinding.ActivityHomeBinding
import com.mshell.discountcalculator.databinding.FragmentItemDetailBottomBinding
import com.mshell.discountcalculator.utils.config.Config.DEFAULT_DOUBLE_VALUE
import com.mshell.discountcalculator.utils.config.Config.DEFAULT_DOUBLE_VALUE_ONE
import com.mshell.discountcalculator.utils.config.DiscountType

class CaldisDataSource {

    private var itemIdAutoIncrement = 0

    fun createNewItem(shoppingItem: ShoppingItem? = null): ShoppingItem {
        itemIdAutoIncrement += 1
        val item = shoppingItem ?: ShoppingItem()
        return item.apply {
            id = itemIdAutoIncrement
            totalPrice = quantity?.times(pricePerUnit ?: DEFAULT_DOUBLE_VALUE)
        }
    }

    fun calculateShoppingDetail(shoppingDetail: ShoppingDetail?): Result<ShoppingDetail?> {
        try {
            shoppingDetail?.apply {
                totalQuantity = listItem?.sumOf { it.quantity ?: DEFAULT_DOUBLE_VALUE }
                totalShopping = listItem?.sumOf { it.totalPrice ?: DEFAULT_DOUBLE_VALUE }
                total = totalShopping?.plus(additional ?: DEFAULT_DOUBLE_VALUE)
            }
            shoppingDetail?.discountDetail.let {
                when(it?.discountType) {
                    DiscountType.NOMINAL -> {
                        return calculateDiscountNominal(shoppingDetail, it.discountNominal)
                    }
                    DiscountType.PERCENT -> {
                        return calculateDiscountPercent(shoppingDetail, it.discountPercent?.toDouble(), it.discountMax)
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
        shoppingDetail: ShoppingDetail?,
        discountNominal: Double?
    ): Result<ShoppingDetail?> {
        return try {
            val discount = discountNominal ?: DEFAULT_DOUBLE_VALUE
            val totalQuantity = shoppingDetail?.totalQuantity ?: DEFAULT_DOUBLE_VALUE
            val discountPerItem = discountNominal?.div(totalQuantity)
            val sumAllItems = shoppingDetail?.totalShopping ?: DEFAULT_DOUBLE_VALUE
            shoppingDetail?.listItem?.forEach { shoppingItem ->
                shoppingItem.apply {
                    totalDiscount = if (sumAllItems < discount) {
                        totalPrice
                    } else {
                        discountPerItem?.times(quantity ?: DEFAULT_DOUBLE_VALUE)
                    }
                    totalPriceAfterDiscount = totalPrice?.let { totalValue ->
                        totalValue - (totalDiscount ?: DEFAULT_DOUBLE_VALUE)
                    }?.takeIf { it > DEFAULT_DOUBLE_VALUE } ?: DEFAULT_DOUBLE_VALUE
                }
            }
            shoppingDetail?.apply {
                this.discount = listItem?.sumOf { it.totalDiscount ?: DEFAULT_DOUBLE_VALUE }
                totalAfterDiscount = total?.minus(this.discount ?: DEFAULT_DOUBLE_VALUE)
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
            val tempDiscount = (discountPercent?.div(100))?.times(totalAllItem) ?: DEFAULT_DOUBLE_VALUE
            val totalDiscount = minOf(tempDiscount, discountMax ?: DEFAULT_DOUBLE_VALUE)

            // Apply discount to each item
            shoppingDetail?.listItem?.forEach { shoppingItem ->
                val proportion = shoppingItem.totalPrice?.div(totalAllItem) ?: DEFAULT_DOUBLE_VALUE
                val itemDiscount = (proportion * totalDiscount)
                shoppingItem.apply {
                    this.totalDiscount = itemDiscount
                    totalPriceAfterDiscount = totalPrice?.minus(this.totalDiscount ?: DEFAULT_DOUBLE_VALUE)
                }
            }
            shoppingDetail?.apply {
                discount = listItem?.sumOf { it.totalDiscount ?: DEFAULT_DOUBLE_VALUE }
                totalAfterDiscount = total?.minus(discount ?: DEFAULT_DOUBLE_VALUE)
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
                ShoppingDetail().apply {
                    this.discountDetail = discountDetail
                    additional = binding.edAdditional.edCurrency.getCleanText().toDouble()
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getItemDetail(binding: FragmentItemDetailBottomBinding): Result<ShoppingItem?> {
        return try {
            Result.success(
                ShoppingItem().apply {
                    itemName = binding.edItemName.text.toString()
                    pricePerUnit = binding.edPricePerUnit.edCurrency.getCleanText().toDouble()
                    quantity = DEFAULT_DOUBLE_VALUE_ONE
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}