package com.mshell.discountcalculator.core.data.source.local.room

import androidx.room.*
import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingItemEntity
import com.mshell.discountcalculator.core.models.ShoppingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CalDisDao {

    @Query("SELECT * FROM shopping_item")
    fun getAllShoppingItem(): Flow<List<ShoppingItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListShoppingItem(list: List<ShoppingItemEntity>)

    @Update
    fun updateShopping(shoppingItem: ShoppingItemEntity)
}