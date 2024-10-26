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
            itemId = itemIdAutoIncrement
            total = itemQuantity?.times(itemPrice ?: DEFAULT_DOUBLE_VALUE)
        }
    }


//    fun createNewList(count: Int): Result<MutableList<Form>> {
//        return try {
//            createNewItem().map { form ->
//                mutableListOf<Form>().apply {
//                    repeat(count) {
//                        add(form)
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Result.failure(e)
//        }
//    }

    fun calculateShoppingDetail(shoppingDetail: ShoppingDetail?): Result<ShoppingDetail?> {
        try {
            shoppingDetail?.apply {
                totalQuantity = listItem?.sumOf { it.itemQuantity ?: DEFAULT_DOUBLE_VALUE }
                total = listItem?.sumOf { it.total ?: DEFAULT_DOUBLE_VALUE }
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
            val sumAllItems = shoppingDetail?.total ?: DEFAULT_DOUBLE_VALUE
            shoppingDetail?.listItem?.forEach { shoppingItem ->
                shoppingItem.apply {
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
            val totalAllItem = shoppingDetail?.total ?: DEFAULT_DOUBLE_VALUE
            // Calculate discount
            val tempDiscount = (discountPercent?.div(100))?.times(totalAllItem) ?: DEFAULT_DOUBLE_VALUE
            val totalDiscount = minOf(tempDiscount, discountMax ?: DEFAULT_DOUBLE_VALUE)

            // Apply discount to each item
            shoppingDetail?.listItem?.forEach { shoppingItem ->
                val proportion = shoppingItem.total?.div(totalAllItem) ?: DEFAULT_DOUBLE_VALUE
                val itemDiscount = (proportion * totalDiscount)
                shoppingItem.apply {
                    this.itemDiscount = itemDiscount
                    afterDiscount = total?.minus(this.itemDiscount ?: DEFAULT_DOUBLE_VALUE)
                }
            }
            // Return updated list
            Result.success(shoppingDetail)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

//    fun calculateDiscountNominal(
//        list: MutableList<ShoppingItem>?,
//        discountNominal: Double?
//    ): Result<MutableList<ShoppingItem>?> {
//        return try {
//            val discount = discountNominal ?: DEFAULT_DOUBLE_VALUE
//            val totalQuantity =
//                list?.sumOf { it.itemQuantity ?: DEFAULT_DOUBLE_VALUE } ?: DEFAULT_DOUBLE_VALUE
//            val discountPerItem = discountNominal?.div(totalQuantity)
//            val sumAllItems = list?.sumOf { form ->
//                (form.itemPrice ?: DEFAULT_DOUBLE_VALUE) * (form.itemQuantity
//                    ?: DEFAULT_DOUBLE_VALUE)
//            } ?: DEFAULT_DOUBLE_VALUE
//
//            list?.forEach { form ->
//                form.apply {
//                    total = itemPrice?.times(itemQuantity ?: DEFAULT_DOUBLE_VALUE)
//                    itemDiscount = if (sumAllItems < discount) {
//                        total
//                    } else {
//                        discountPerItem?.times(itemQuantity ?: DEFAULT_DOUBLE_VALUE)
//                    }
//                    afterDiscount = total?.let { totalValue ->
//                        totalValue - (itemDiscount ?: DEFAULT_DOUBLE_VALUE)
//                    }?.takeIf { it > DEFAULT_DOUBLE_VALUE } ?: DEFAULT_DOUBLE_VALUE
//                }
//            }
//
//            Result.success(list)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Result.failure(e)
//        }
//    }

//    fun calculateDiscountPercent(
//        list: MutableList<ShoppingItem>?,
//        discountPercent: Double?,
//        discountMax: Double?
//    ): Result<MutableList<ShoppingItem>?> {
//        return try {
//            var totalAllItem = DEFAULT_DOUBLE_VALUE
//            // Calculate total for all items
//            list?.forEach { form ->
//                form.total = form.itemPrice?.times(form.itemQuantity ?: DEFAULT_DOUBLE_VALUE)
//                totalAllItem += form.total ?: DEFAULT_DOUBLE_VALUE
//            }
//
//            // Calculate discount
//            val tempDiscount =
//                (discountPercent?.div(100))?.times(totalAllItem) ?: DEFAULT_DOUBLE_VALUE
//            val totalDiscount = minOf(tempDiscount, discountMax ?: DEFAULT_DOUBLE_VALUE)
//
//            // Apply discount to each item
//            list?.forEach { form ->
//                val proportion = form.total?.div(totalAllItem) ?: DEFAULT_DOUBLE_VALUE
//                val itemDiscount = (proportion * totalDiscount)
//                form.apply {
//                    this.itemDiscount = itemDiscount
//                    afterDiscount = total?.minus(this.itemDiscount ?: DEFAULT_DOUBLE_VALUE)
//                }
//            }
//            // Return updated list
//            Result.success(list)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Result.failure(e)
//        }
//    }

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
                    itemPrice = binding.edItemPrice.edCurrency.getCleanText().toDouble()
                    itemQuantity = DEFAULT_DOUBLE_VALUE_ONE
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}