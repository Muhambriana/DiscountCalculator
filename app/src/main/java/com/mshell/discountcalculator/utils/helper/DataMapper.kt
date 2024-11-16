package com.mshell.discountcalculator.utils.helper

import com.mshell.discountcalculator.core.data.source.local.entity.DiscountDetailEntity
import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingEntity
import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingItemEntity
import com.mshell.discountcalculator.core.data.source.local.entity.relation.ShoppingWithDiscountDetailAndItems
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
                shoppingId = it.shoppingId,
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

    fun mapDomainToEntities(input: List<ShoppingItem>): List<ShoppingItemEntity> =
        input.map {
            ShoppingItemEntity(
                shoppingItemId = it.id,
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

    fun mapDomainToEntity(input: ShoppingItem) = ShoppingItemEntity (
        shoppingItemId = input.id,
        shoppingId = input.shoppingId,
        itemName = input.itemName,
        pricePerUnit = input.pricePerUnit,
        quantity = input.quantity,
        totalPrice = input.totalPrice,
        totalDiscount = input.totalDiscount,
        discountPerUnit = input.discountPerUnit,
        pricePerUnitAfterDiscount = input.pricePerUnitAfterDiscount,
        totalPriceAfterDiscount = input.totalPriceAfterDiscount
    )


    fun mapDomainToEntity(input: ShoppingDetail) = ShoppingEntity(
        shoppingId = input.shoppingId,
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

    private fun mapEntityToDomain(input: DiscountDetailEntity) = DiscountDetail(
        discountDetailId = input.discountDetailId,
        shoppingId = input.shoppingId,
        discountType = input.discountType,
        discountNominal = input.discountNominal,
        discountPercent = input.discountPercent,
        discountMax = input.discountMax
    )

    fun mapEntityToDomain(input: ShoppingEntity, input2: DiscountDetailEntity) = ShoppingDetail(
        shoppingId = input.shoppingId,
        discountDetail = mapEntityToDomain(input2),
        totalShopping = input.totalShopping,
        additional = input.additional,
        total = input.total,
        totalQuantity = input.totalQuantity,
        discount = input.discount,
        totalAfterDiscount = input.totalAfterDiscount
    )

    fun mapRelationEntityToDomain(input: ShoppingWithDiscountDetailAndItems) = ShoppingDetail(
        shoppingId = input.shopping.shoppingId,
        discountDetail = mapEntityToDomain(input.discountDetail),
        totalShopping = input.shopping.totalShopping,
        additional = input.shopping.additional,
        total = input.shopping.total,
        totalQuantity = input.shopping.totalQuantity,
        discount = input.shopping.discount,
        totalAfterDiscount = input.shopping.totalAfterDiscount,
        listItem = mapEntitiesToDomain(input.shoppingItems).toMutableList()
    )

    fun mapDomainToRelationEntity(input: ShoppingDetail) = ShoppingWithDiscountDetailAndItems(
        shopping = mapDomainToEntity(input),
        discountDetail = mapDomainToEntity(input.discountDetail),
        shoppingItems = mapDomainToEntities(input.listItem)
    )

}