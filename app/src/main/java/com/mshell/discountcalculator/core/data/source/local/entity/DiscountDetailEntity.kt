package com.mshell.discountcalculator.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mshell.discountcalculator.utils.config.DiscountType

@Entity(
    tableName = "discount_detail",
    foreignKeys = [ForeignKey(
        entity = ShoppingEntity::class,
        parentColumns = ["shopping_id"],
        childColumns = ["shopping_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(
        value = ["discount_detail_id", "shopping_id"]
    )]
)
data class DiscountDetailEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "discount_detail_id")
    val discountDetailId: Long = 0,

    @ColumnInfo(name = "shopping_id")
    val shoppingId: Long = 0,

    @ColumnInfo(name = "discount_type")
    val discountType: DiscountType? = null,

    @ColumnInfo(name = "discount_nominal")
    val discountNominal: Double? = null,

    @ColumnInfo(name = "discount_percent")
    val discountPercent: Int? = null,

    @ColumnInfo(name = "discount_max")
    val discountMax: Double? = null
)
