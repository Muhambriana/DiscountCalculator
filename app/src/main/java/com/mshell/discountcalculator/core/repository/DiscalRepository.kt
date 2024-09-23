package com.mshell.discountcalculator.core.repository

import com.mshell.discountcalculator.core.models.Form
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class DiscalRepository {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        println("Caught exception: $throwable")
    }
    private val dispatchers = (Dispatchers.IO + handler)

    fun getItem(): Form {
        return Form()
    }

    fun getFirstList(count: Int): MutableList<Form> {
        val list = mutableListOf<Form>()
        for (i in 1..count) {
            list.add(getItem())
        }
        return list
    }

    suspend fun getDiscountPercentResult(
        list: MutableList<Form>?,
        discountPercent: Double?,
        discountMax: Double?
    ): MutableList<Form>? {
        val result = withContext(dispatchers) {
            var totalAllItem = 0.0
            async {
                list?.forEach {
                    it.total = it.itemPrice?.times(it.itemQuantity ?: 0.0)
                    totalAllItem += it.total ?: 0.0
                }
            }.await()
            val tempDiscount = ((discountPercent?.div(100))?.times(totalAllItem))
            val totalDiscount = minOf(tempDiscount ?: 0.0, discountMax ?: 0.0)

            async {
                list?.forEach {
                    val firstCalculation = (it.total?.div(totalAllItem))
                    val secondCalculation = (firstCalculation)?.times(100)
                    it.discount = (secondCalculation?.times(totalDiscount))?.div(100)
                }
            }.await()

            list
        }
        return result

    }

}