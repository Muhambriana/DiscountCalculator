package com.mshell.discountcalculator.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "shopping_item",
    foreignKeys = [ForeignKey(
        entity = ShoppingEntity::class,
        parentColumns = ["shopping_id"],
        childColumns = ["shopping_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index(value = ["shopping_item_id"]),    // Index on primary key for fast lookups
        Index(value = ["shopping_id"])            // Separate index on foreign key column to prevent full table scans
    ]
)
data class ShoppingItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "shopping_item_id")
    var shoppingItemId: Long = 0,

    @ColumnInfo(name = "shopping_id")
    var shoppingId: Long = 0,

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
