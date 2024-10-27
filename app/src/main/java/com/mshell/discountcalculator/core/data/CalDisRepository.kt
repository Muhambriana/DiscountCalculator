package com.mshell.discountcalculator.core.data

import com.mshell.discountcalculator.core.data.source.local.CalDisDataSource
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.databinding.ActivityHomeBinding
import com.mshell.discountcalculator.databinding.FragmentItemDetailBottomBinding

class CalDisRepository(private val caldisDataSource: CalDisDataSource) {

    fun getNewItem(shoppingItem: ShoppingItem? = null) =
        caldisDataSource.createNewItem(shoppingItem)

    fun getDiscountResult(shoppingDetail: ShoppingDetail?): Result<ShoppingDetail?> =
        caldisDataSource.calculateShoppingDetail(shoppingDetail)

    fun getShoppingDetail(binding: ActivityHomeBinding): Result<ShoppingDetail?> =
        caldisDataSource.getShoppingDetail(binding)

    fun getItemDetail(binding: FragmentItemDetailBottomBinding): Result<ShoppingItem?> =
        caldisDataSource.getItemDetail(binding)

}