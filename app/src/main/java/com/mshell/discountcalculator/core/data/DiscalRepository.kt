package com.mshell.discountcalculator.core.data

import com.mshell.discountcalculator.core.data.source.local.CaldisDataSource
import com.mshell.discountcalculator.core.models.DiscountDetail
import com.mshell.discountcalculator.core.models.Form
import com.mshell.discountcalculator.databinding.ActivityHomeBinding
import com.mshell.discountcalculator.databinding.FragmentItemDetailBottomBinding

class DiscalRepository(private val caldisDataSource: CaldisDataSource) {

    fun getNewItem(form: Form? = null) =
        caldisDataSource.createNewItem(form)

//    fun getFirstList(count: Int): Result<MutableList<Form>> =
//        caldisDataSource.createNewList(count)

    fun getDiscountPercentResult(
        list: MutableList<Form>?,
        discountPercent: Double?,
        discountMax: Double?
    ): Result<MutableList<Form>?> =
        caldisDataSource.calculateDiscountPercent(list, discountPercent, discountMax)

    fun getDiscountNominalResult(
        list: MutableList<Form>?,
        discountNominal: Double?
    ): Result<MutableList<Form>?> =
        caldisDataSource.calculateDiscountNominal(list, discountNominal)

    fun getDiscountDetail(binding: ActivityHomeBinding): Result<DiscountDetail?> =
        caldisDataSource.getDiscountDetail(binding)

    fun getItemDetail(binding: FragmentItemDetailBottomBinding): Result<Form?> =
        caldisDataSource.getItemDetail(binding)

}