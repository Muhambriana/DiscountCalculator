package com.mshell.discountcalculator.core.data.source.local.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.mshell.discountcalculator.core.data.source.local.entity.DiscountDetailEntity
import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingEntity
import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingItemEntity

data class ShoppingWithDiscountDetailAndItems(
    @Embedded val shopping: ShoppingEntity,

    // 1 to 1 relation
    @Relation(
        parentColumn = "shopping_id",
        entityColumn = "shopping_id"
    )
    val discountDetail: DiscountDetailEntity,

    // 1 to many relation
    @Relation(
        parentColumn = "shopping_id",
        entityColumn = "shopping_id"
    )
    val shoppingItems: List<ShoppingItemEntity>
)
