package com.mshell.discountcalculator.core.data.source.local

import com.mshell.discountcalculator.core.data.source.local.entity.DiscountDetailEntity
import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingEntity
import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingItemEntity
import com.mshell.discountcalculator.core.data.source.local.room.CalDisDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val calDisDao: CalDisDao) {

    suspend fun insertListShoppingItem(list: List<ShoppingItemEntity>) = calDisDao.insertListShoppingItem(list)

    suspend fun insertShopping(entity: ShoppingEntity) = calDisDao.insertShopping(entity)

    suspend fun insertDiscountDetail(entity: DiscountDetailEntity) = calDisDao.insertDiscountDetail(entity)

    suspend fun updateShoppingAndDiscount(shoppingEntity: ShoppingEntity, discountDetailEntity: DiscountDetailEntity) = calDisDao.updateShoppingAndDiscount(shoppingEntity, discountDetailEntity)

    fun getDiscountDetailByShoppingId(shoppingId: Long) = calDisDao.getDiscountDetailByShoppingId(shoppingId)

    fun getShoppingDetailById(shoppingId: Long) = calDisDao.getShoppingById(shoppingId)

    fun getAllShoppingItem(): Flow<List<ShoppingItemEntity>> = calDisDao.getAllShoppingItem()

}