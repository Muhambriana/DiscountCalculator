package com.mshell.discountcalculator.core.di.domain.usecase

import com.mshell.discountcalculator.core.di.domain.repository.InterfaceCalDisRepository
import com.mshell.discountcalculator.core.models.ShoppingItem


class CalDisInteractor(private val iCalDisRepository: InterfaceCalDisRepository): CalDisUseCase {

    override suspend fun insertListShoppingItem(list: List<ShoppingItem>) =
        iCalDisRepository.insertListShoppingItem(list)

    override fun getAllShoppingItem() =
        iCalDisRepository.getAllShoppingItem()

}