package com.mshell.discountcalculator.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_item")
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "item_name")
    var itemName: String? = null,

    @ColumnInfo(name = "price_per_unit")
    var pricePerUnit: Double? = null,

    @ColumnInfo(name = "quantity")
    var quantity: Double? = null,

    @ColumnInfo(name = "total_price")
    var totalPrice: Double? = null,

    @ColumnInfo(name = "total_discount")
    var totalDiscount: Double? = null,

    @ColumnInfo(name = "discount_per_unit")
    var discountPerUnit: Double? = null,

    @ColumnInfo(name = "price_per_unit_after_discount")
    var pricePerUnitAfterDiscount: Double? = null,

    @ColumnInfo(name = "total_price_after_discount")
    var totalPriceAfterDiscount: Double? = null
)
