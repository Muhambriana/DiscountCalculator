package com.mshell.discountcalculator.utils.helper

import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingItemEntity
import com.mshell.discountcalculator.core.models.ShoppingItem

object DataMapper {

    fun mapItemsToEntities(input: List<ShoppingItem>): List<ShoppingItemEntity> {
        val entityList = ArrayList<ShoppingItemEntity>()
        input.map {
            val itemEntity = ShoppingItemEntity(
                id = it.id ?: 0,
                itemName = it.itemName ?: "",
                pricePerUnit = it.pricePerUnit ?: 0.0,
                quantity = it.quantity ?: 0.0,
                totalPrice = it.totalPrice ?: 0.0,
                totalDiscount = it.totalDiscount ?: 0.0,
                discountPerUnit = it.discountPerUnit ?: 0.0,
                pricePerUnitAfterDiscount = it.pricePerUnitAfterDiscount ?: 0.0,
                totalPriceAfterDiscount = it.totalPriceAfterDiscount ?: 0.0
            )
            entityList.add(itemEntity)
        }
        return entityList
    }

    fun mapEntitiesToDomain(input: List<ShoppingItemEntity>): List<ShoppingItem> =
        input.map {
            ShoppingItem(
                id = it.id,
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

    fun mapDomainToEntity(input: ShoppingItem) = ShoppingItemEntity(
        id = input.id ?: 0,
        itemName = input.itemName ?: "",
        pricePerUnit = input.pricePerUnit ?: 0.0,
        quantity = input.quantity ?: 0.0,
        totalPrice = input.totalPrice ?: 0.0,
        totalDiscount = input.totalDiscount ?: 0.0,
        discountPerUnit = input.discountPerUnit ?: 0.0,
        pricePerUnitAfterDiscount = input.pricePerUnitAfterDiscount ?: 0.0,
        totalPriceAfterDiscount = input.totalPriceAfterDiscount ?: 0.0
    )

}