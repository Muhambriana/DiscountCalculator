package com.mshell.discountcalculator.core.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoppingDetail(

    var discountDetail: DiscountDetail? = null,

    var total: Double? = null,

    var additional: Double? = null,

    var totalQuantity: Double? = null,

    var totalAfterDiscount: Double? = null

) : Parcelable