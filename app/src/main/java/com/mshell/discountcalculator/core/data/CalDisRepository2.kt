package com.mshell.discountcalculator.core.data

import com.mshell.discountcalculator.core.data.source.CalDisResource
import com.mshell.discountcalculator.core.data.source.local.LocalDataSource
import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingItemEntity
import com.mshell.discountcalculator.core.di.domain.repository.InterfaceCalDisRepository
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.utils.AppExecutors
import com.mshell.discountcalculator.utils.helper.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalDisRepository2(
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
): InterfaceCalDisRepository {
    override suspend fun insertListShoppingItem(list: List<ShoppingItem>) {
        val shoppingItemList = DataMapper.mapItemsToEntities(list)
        localDataSource.insertListShoppingItem(shoppingItemList)
    }

    override fun getAllShoppingItem(): Flow<List<ShoppingItem>> {
        return localDataSource.getAllShoppingItem().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }


}