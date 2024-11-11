package com.mshell.discountcalculator.core.data

import com.mshell.discountcalculator.core.CalDisEvent
import com.mshell.discountcalculator.core.data.source.CalDisResource
import com.mshell.discountcalculator.core.data.source.local.LocalDataSource
import com.mshell.discountcalculator.core.di.domain.repository.InterfaceCalDisRepository
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.utils.AppExecutors
import com.mshell.discountcalculator.utils.config.ExceptionTypeEnum
import com.mshell.discountcalculator.utils.helper.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CalDisRepository2(
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
): InterfaceCalDisRepository {

    override suspend fun insertListShoppingItem(list: List<ShoppingItem>) {
        val shoppingItemList = DataMapper.mapItemsToEntities(list)
        localDataSource.insertListShoppingItem(shoppingItemList)
    }

    override suspend fun insertShoppingDetail(shoppingDetail: ShoppingDetail): CalDisEvent<CalDisResource<Long>> {
        return try {
            val shopping = DataMapper.mapDomainToEntity(shoppingDetail)
            val shoppingId = localDataSource.insertShopping(shopping)
            val discountDetail = shoppingDetail.discountDetail.apply {
                this.shoppingId = shoppingId
            }
            val discountEntity = DataMapper.mapDomainToEntity(discountDetail)
            localDataSource.insertDiscountDetail(discountEntity)
            CalDisEvent(CalDisResource.Success(shoppingId))
        } catch (e: Exception) {
            e.printStackTrace()
            CalDisEvent(CalDisResource.Error(null, e, ExceptionTypeEnum.RESULT_ERROR_2))
        }
    }

    override suspend fun updateShoppingAndDiscount(shoppingDetail: ShoppingDetail): CalDisEvent<CalDisResource<Boolean>> {
        return try {
            val shoppingEntity = DataMapper.mapDomainToEntity(shoppingDetail)
            val discountDetailEntity = DataMapper.mapDomainToEntity(shoppingDetail.discountDetail)
            val success = localDataSource.updateShoppingAndDiscount(shoppingEntity, discountDetailEntity)

            if (success.not()) {
                return CalDisEvent(CalDisResource.Error(exceptionTypeEnum = ExceptionTypeEnum.RESULT_ERROR_TRANSACTION))
            }

            CalDisEvent(CalDisResource.Success(success))
        } catch (e: Exception) {
            e.printStackTrace()
            CalDisEvent(CalDisResource.Error(error = e, exceptionTypeEnum =  ExceptionTypeEnum.RESULT_ERROR_2))
        }
    }

    override suspend fun getShoppingDetailById(shoppingId: Long): CalDisEvent<CalDisResource<ShoppingDetail>> {
        return try {
            val shopping = getFromDiskIO { localDataSource.getShoppingWithDiscountDetailAndItems(shoppingId) }
            val shoppingDetail = DataMapper.mapRelationEntityToDomain(shopping)

            CalDisEvent(CalDisResource.Success(shoppingDetail))
        } catch (e: Exception) {
            e.printStackTrace()
            CalDisEvent(CalDisResource.Error(error = e, exceptionTypeEnum =  ExceptionTypeEnum.RESULT_ERROR_2))
        }
    }

    override fun getAllShoppingItem(): Flow<List<ShoppingItem>> =
        localDataSource.getAllShoppingItem().map {
            DataMapper.mapEntitiesToDomain(it)
        }


    // Helper function to run a suspend block on diskIO
    private suspend fun <T> getFromDiskIO(block: () -> T): T = suspendCoroutine { continuation ->
        appExecutors.diskIO().execute {
            try {
                continuation.resume(block())
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }
    }
}