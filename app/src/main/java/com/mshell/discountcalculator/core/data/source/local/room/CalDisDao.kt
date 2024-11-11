package com.mshell.discountcalculator.core.data.source.local.room

import androidx.room.*
import com.mshell.discountcalculator.core.data.source.local.entity.DiscountDetailEntity
import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingEntity
import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingItemEntity
import com.mshell.discountcalculator.core.data.source.local.entity.relation.ShoppingWithDiscountDetailAndItems
import kotlinx.coroutines.flow.Flow

@Dao
interface CalDisDao {

    @Query("SELECT * FROM shopping_item")
    fun getAllShoppingItem(): Flow<List<ShoppingItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopping(entity: ShoppingEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscountDetail(entity: DiscountDetailEntity)

    @Query("SELECT * FROM discount_detail where shopping_id = :shoppingId")
    fun getDiscountDetailByShoppingId(shoppingId: Long): DiscountDetailEntity?

    @Query("SELECT * FROM shopping where shopping_id = :shoppingId")
    fun getShoppingById(shoppingId: Long): ShoppingEntity?

    @Transaction
    @Query("SELECT * FROM shopping WHERE shopping_id = :shoppingId")
    fun getShoppingWithDiscountDetailAndItems(shoppingId: Long): ShoppingWithDiscountDetailAndItems

    @Update
    suspend fun updateDiscountDetail(entity: DiscountDetailEntity): Int

    @Update
    suspend fun updateShopping(entity: ShoppingEntity): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListShoppingItem(list: List<ShoppingItemEntity>)

    @Transaction
    suspend fun updateShoppingAndDiscount(shoppingEntity: ShoppingEntity, discountDetailEntity: DiscountDetailEntity): Boolean {
        return try {
            // Perform operations inside the transaction
            val rowAffected = updateShopping(shoppingEntity)
            val rowAffected2 = updateDiscountDetail(discountDetailEntity)

            if (rowAffected <= 0 || rowAffected2 <= 0) {
                return false
            }

            // If no exception is thrown, return true (success)
            true
        } catch (e: Exception) {
            // If an exception occurs, Room automatically rolls back the transaction
            e.printStackTrace()
            false // Indicate failure
        }
    }

}