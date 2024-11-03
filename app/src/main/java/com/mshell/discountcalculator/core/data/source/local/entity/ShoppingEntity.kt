package com.mshell.discountcalculator.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "shopping",
    indices = [Index(
        value = ["shopping_id"]
    )]
)
data class ShoppingEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "shopping_id")
    var shoppingId: Long = 0,

    @ColumnInfo(name = "total_shopping")
    var totalShopping: Double? = null,

    @ColumnInfo(name = "additional")
    var additional: Double? = null,

    @ColumnInfo(name = "total")
    var total: Double? = null,

    @ColumnInfo(name = "total_quantity")
    var totalQuantity: Double? = null,

    @ColumnInfo(name = "discount")
    var discount: Double? = null,

    @ColumnInfo(name = "total_after_discount")
    var totalAfterDiscount: Double? = null
)

