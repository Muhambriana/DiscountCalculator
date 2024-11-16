package com.mshell.discountcalculator.core.models

import android.os.Parcelable
import com.mshell.discountcalculator.utils.config.DiscountType
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountDetail(

    var discountDetailId: Long = 0,

    var shoppingId: Long = 0,

    var discountType: DiscountType? = null,

    var discountNominal: Double? = null,

    var discountPercent: Int? = null,

    var discountMax: Double? = null,

) : Parcelable
