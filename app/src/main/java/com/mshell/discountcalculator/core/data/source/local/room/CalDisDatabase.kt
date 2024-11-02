package com.mshell.discountcalculator.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingItemEntity

@Database(entities = [ShoppingItemEntity::class], version = 1, exportSchema = false)
abstract class CalDisDatabase : RoomDatabase() {

    abstract fun calDisDao(): CalDisDao

}