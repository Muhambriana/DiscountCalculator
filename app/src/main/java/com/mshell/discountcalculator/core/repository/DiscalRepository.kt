package com.mshell.discountcalculator.core.repository

import com.mshell.discountcalculator.core.datasource.CaldisDataSource
import com.mshell.discountcalculator.core.models.DiscountDetail
import com.mshell.discountcalculator.core.models.Form
import com.mshell.discountcalculator.databinding.ActivityHomeBinding
import com.mshell.discountcalculator.databinding.FragmentItemDetailBottomBinding

class DiscalRepository(private val caldisDataSource: CaldisDataSource) {

    fun getNewItem(form: Form? = null): Result<Form> =
        caldisDataSource.createNewItem(form)

    fun getFirstList(count: Int): Result<MutableList<Form>> =
        caldisDataSource.createNewList(count)

    fun getDiscountPercentResult(
        list: MutableList<Form>?,
        discountPercent: Double?,
        discountMax: Double?
    ): Result<MutableList<Form>?> =
        caldisDataSource.calculateDiscountPercent(list, discountPercent, discountMax)

    suspend fun getDiscountNominalResult(
        list: MutableList<Form>?,
        discountNominal: Double?
    ): Result<MutableList<Form>?> =
        caldisDataSource.calculateDiscountNominal(list, discountNominal)

    fun getDiscountDetail(binding: ActivityHomeBinding): Result<DiscountDetail?> =
        caldisDataSource.getDiscountDetail(binding)

    fun getItemDetail(binding: FragmentItemDetailBottomBinding): Result<Form?> =
        caldisDataSource.getItemDetail(binding)

}