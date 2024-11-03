package com.mshell.discountcalculator.core.data.source.local.room

import androidx.room.*
import com.mshell.discountcalculator.core.data.source.local.entity.DiscountDetailEntity
import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingEntity
import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingItemEntity
import com.mshell.discountcalculator.core.models.DiscountDetail
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.core.models.ShoppingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CalDisDao {

    @Query("SELECT * FROM shopping_item")
    fun getAllShoppingItem(): Flow<List<ShoppingItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopping(entity: ShoppingEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscountDetail(entity: DiscountDetailEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListShoppingItem(list: List<ShoppingItemEntity>)

    @Update
    fun updateShopping(shoppingItem: ShoppingItemEntity)
}