package com.mshell.discountcalculator.core.data.source.local

import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingItemEntity
import com.mshell.discountcalculator.core.data.source.local.room.CalDisDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val calDisDao: CalDisDao) {

    suspend fun insertListShoppingItem(list: List<ShoppingItemEntity>) = calDisDao.insertListShoppingItem(list)

    fun getAllShoppingItem(): Flow<List<ShoppingItemEntity>> = calDisDao.getAllShoppingItem()

}