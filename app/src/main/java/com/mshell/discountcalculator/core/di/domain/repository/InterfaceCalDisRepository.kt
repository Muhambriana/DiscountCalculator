package com.mshell.discountcalculator.core.di.domain.repository

import com.mshell.discountcalculator.core.CalDisEvent
import com.mshell.discountcalculator.core.data.source.CalDisResource
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.core.models.ShoppingItem
import kotlinx.coroutines.flow.Flow

interface InterfaceCalDisRepository {

    suspend fun insertListShoppingItem(list: List<ShoppingItem>)

    suspend fun insertShoppingDetail(shoppingDetail: ShoppingDetail): CalDisEvent<CalDisResource<Long>>

    fun getAllShoppingItem(): Flow<List<ShoppingItem>>

}