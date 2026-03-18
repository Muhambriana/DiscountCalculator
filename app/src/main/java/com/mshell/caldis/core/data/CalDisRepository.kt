package com.mshell.caldis.core.data

import com.mshell.caldis.core.data.source.local.CalDisDataSource
import com.mshell.caldis.core.models.ShoppingItem
import com.mshell.caldis.core.models.ShoppingDetail
import com.mshell.caldis.databinding.ActivityHomeBinding
import com.mshell.caldis.databinding.FragmentItemDetailBottomBinding

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