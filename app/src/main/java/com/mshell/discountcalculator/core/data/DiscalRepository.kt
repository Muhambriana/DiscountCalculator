package com.mshell.discountcalculator.core.data

import com.mshell.discountcalculator.core.data.source.local.CaldisDataSource
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.databinding.ActivityHomeBinding
import com.mshell.discountcalculator.databinding.FragmentItemDetailBottomBinding

class DiscalRepository(private val caldisDataSource: CaldisDataSource) {

    fun getNewItem(shoppingItem: ShoppingItem? = null) =
        caldisDataSource.createNewItem(shoppingItem)

//    fun getFirstList(count: Int): Result<MutableList<Form>> =
//        caldisDataSource.createNewList(count)

    fun getDiscountPercentResult(
        list: MutableList<ShoppingItem>?,
        discountPercent: Double?,
        discountMax: Double?
    ): Result<MutableList<ShoppingItem>?> =
        caldisDataSource.calculateDiscountPercent(list, discountPercent, discountMax)

    fun getDiscountNominalResult(
        list: MutableList<ShoppingItem>?,
        discountNominal: Double?
    ): Result<MutableList<ShoppingItem>?> =
        caldisDataSource.calculateDiscountNominal(list, discountNominal)

    fun getShoppingDetail(binding: ActivityHomeBinding): Result<ShoppingDetail?> =
        caldisDataSource.getShoppingDetail(binding)

    fun getItemDetail(binding: FragmentItemDetailBottomBinding): Result<ShoppingItem?> =
        caldisDataSource.getItemDetail(binding)

}