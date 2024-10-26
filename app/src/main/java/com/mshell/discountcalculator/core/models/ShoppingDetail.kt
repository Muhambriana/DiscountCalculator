package com.mshell.discountcalculator.core.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoppingDetail(

    var discountDetail: DiscountDetail? = null,

    var totalShopping: Double? = null,

    var additional: Double? = null,

    var total: Double? = null,

    var totalQuantity: Double? = null,

    var discount: Double? = null,

    var totalAfterDiscount: Double? = null,

    var listItem: MutableList<ShoppingItem>? = null

) : Parcelable