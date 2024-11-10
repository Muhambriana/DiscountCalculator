package com.mshell.discountcalculator.core.di.domain.usecase

import com.mshell.discountcalculator.core.CalDisEvent
import com.mshell.discountcalculator.core.data.source.CalDisResource
import com.mshell.discountcalculator.core.di.domain.repository.InterfaceCalDisRepository
import com.mshell.discountcalculator.core.models.DiscountDetail
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.core.models.ShoppingItem


class CalDisInteractor(private val iCalDisRepository: InterfaceCalDisRepository): CalDisUseCase {

    override suspend fun insertListShoppingItem(list: List<ShoppingItem>) =
        iCalDisRepository.insertListShoppingItem(list)

    override suspend fun insertShoppingDetail(shoppingDetail: ShoppingDetail): CalDisEvent<CalDisResource<Long>> =
        iCalDisRepository.insertShoppingDetail(shoppingDetail)

    override suspend fun updateShoppingAndDiscount(shoppingDetail: ShoppingDetail): CalDisEvent<CalDisResource<Boolean>> =
        iCalDisRepository.updateShoppingAndDiscount(shoppingDetail)

    override suspend fun getShoppingDetailById(shoppingId: Long): CalDisEvent<CalDisResource<ShoppingDetail>> =
        iCalDisRepository.getShoppingDetailById(shoppingId)

    override fun getAllShoppingItem() =
        iCalDisRepository.getAllShoppingItem()

}