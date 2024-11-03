package com.mshell.discountcalculator.core.data

import com.mshell.discountcalculator.core.CalDisEvent
import com.mshell.discountcalculator.core.data.source.CalDisResource
import com.mshell.discountcalculator.core.data.source.local.LocalDataSource
import com.mshell.discountcalculator.core.data.source.local.entity.ShoppingItemEntity
import com.mshell.discountcalculator.core.di.domain.repository.InterfaceCalDisRepository
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.utils.AppExecutors
import com.mshell.discountcalculator.utils.config.ExceptionTypeEnum
import com.mshell.discountcalculator.utils.helper.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class CalDisRepository2(
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
): InterfaceCalDisRepository {
    override suspend fun insertListShoppingItem(list: List<ShoppingItem>) {
        val shoppingItemList = DataMapper.mapItemsToEntities(list)
        localDataSource.insertListShoppingItem(shoppingItemList)
    }

    override suspend fun insertShoppingDetail(shoppingDetail: ShoppingDetail): CalDisEvent<CalDisResource<Long>> {
        try {
            val shopping = DataMapper.mapDomainToEntity(shoppingDetail)
            val shoppingId = localDataSource.insertShopping(shopping)
            val discountDetail = shoppingDetail.discountDetail.apply {
                this.shoppingId = shoppingId
            }
            val discountEntity = DataMapper.mapDomainToEntity(discountDetail)
            localDataSource.insertDiscountDetail(discountEntity)
            return CalDisEvent(CalDisResource.Success(shoppingId))
        } catch (e: Exception) {
            e.printStackTrace()
            return CalDisEvent(CalDisResource.Error(null, e, ExceptionTypeEnum.RESULT_ERROR_2))
        }
    }

    override fun getAllShoppingItem(): Flow<List<ShoppingItem>> =
        localDataSource.getAllShoppingItem().map {
            DataMapper.mapEntitiesToDomain(it)
        }



}