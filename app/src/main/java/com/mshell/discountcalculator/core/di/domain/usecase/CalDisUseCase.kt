package com.mshell.discountcalculator.core.di.domain.usecase

import com.mshell.discountcalculator.core.data.source.CalDisResource
import com.mshell.discountcalculator.core.models.ShoppingItem
import kotlinx.coroutines.flow.Flow

interface CalDisUseCase {
    suspend fun insertListShoppingItem(list: List<ShoppingItem>)
    fun getAllShoppingItem(): Flow<List<ShoppingItem>>
}