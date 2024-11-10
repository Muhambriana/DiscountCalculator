package com.mshell.discountcalculator.core.data

import android.util.Log
import com.mshell.discountcalculator.core.CalDisEvent
import com.mshell.discountcalculator.core.data.source.CalDisResource
import com.mshell.discountcalculator.core.data.source.local.LocalDataSource
import com.mshell.discountcalculator.core.di.domain.repository.InterfaceCalDisRepository
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.utils.AppExecutors
import com.mshell.discountcalculator.utils.config.ExceptionTypeEnum
import com.mshell.discountcalculator.utils.helper.DataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CalDisRepository2(
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
): InterfaceCalDisRepository {

    private val dispatchersIO = Dispatchers.IO

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
            val shoppingEntity = withContext(dispatchersIO) { localDataSource.getShoppingDetailById(shoppingId) }
            val discountDetailEntity = withContext(dispatchersIO) { localDataSource.getDiscountDetailByShoppingId(shoppingId) }

            // If can't find record in DB
            if (shoppingEntity == null || discountDetailEntity == null) {
                return CalDisEvent(CalDisResource.Error(exceptionTypeEnum = ExceptionTypeEnum.RESULT_ERROR_TRANSACTION))
            }
            Log.i("kocak", shoppingEntity.shoppingId.toString())

            val shoppingDetail = DataMapper.mapEntityToDomain(shoppingEntity, discountDetailEntity)
            Log.i("kocak2", shoppingDetail.shoppingId.toString())


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



}