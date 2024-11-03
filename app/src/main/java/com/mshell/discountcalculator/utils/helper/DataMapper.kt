package com.mshell.discountcalculator.utils.helper

import com.mshell.discountcalculator.core.data.source.local.entity.DiscountDetailEntity
import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingEntity
import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingItemEntity
import com.mshell.discountcalculator.core.models.DiscountDetail
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.core.models.ShoppingItem

object DataMapper {

    fun mapItemsToEntities(input: List<ShoppingItem>): List<ShoppingItemEntity> {
        val entityList = ArrayList<ShoppingItemEntity>()
        input.map {
            val itemEntity = ShoppingItemEntity(
                itemName = it.itemName,
                pricePerUnit = it.pricePerUnit,
                quantity = it.quantity,
                totalPrice = it.totalPrice,
                totalDiscount = it.totalDiscount,
                discountPerUnit = it.discountPerUnit,
                pricePerUnitAfterDiscount = it.pricePerUnitAfterDiscount,
                totalPriceAfterDiscount = it.totalPriceAfterDiscount
            )
            entityList.add(itemEntity)
        }
        return entityList
    }

    fun mapEntitiesToDomain(input: List<ShoppingItemEntity>): List<ShoppingItem> =
        input.map {
            ShoppingItem(
                id = it.shoppingItemId,
                itemName = it.itemName,
                pricePerUnit = it.pricePerUnit,
                quantity = it.quantity,
                totalPrice = it.totalPrice,
                totalDiscount = it.totalDiscount,
                discountPerUnit = it.discountPerUnit,
                pricePerUnitAfterDiscount = it.pricePerUnitAfterDiscount,
                totalPriceAfterDiscount = it.totalPriceAfterDiscount
            )
        }


    fun mapDomainToEntity(input: ShoppingDetail) = ShoppingEntity(
        totalShopping = input.totalShopping,
        additional = input.additional,
        total = input.total,
        totalQuantity = input.totalQuantity,
        discount = input.discount,
        totalAfterDiscount = input.totalAfterDiscount
    )

    fun mapDomainToEntity(input: DiscountDetail) = DiscountDetailEntity(
        discountDetailId = input.discountDetailId ?: 0,
        shoppingId = input.shoppingId,
        discountType = input.discountType,
        discountNominal = input.discountNominal,
        discountPercent = input.discountPercent,
        discountMax = input.discountMax
    )

}