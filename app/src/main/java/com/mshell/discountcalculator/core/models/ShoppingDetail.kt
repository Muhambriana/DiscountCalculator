package com.mshell.discountcalculator.core.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoppingDetail(

    var shoppingId: Long = 0,

    var discountDetail: DiscountDetail,

    var totalShopping: Double? = null,

    var additional: Double? = null,

    var total: Double? = null,

    var totalQuantity: Double? = null,

    var discount: Double? = null,

    var totalAfterDiscount: Double? = null,

    var listItem: MutableList<ShoppingItem>? = null

) : Parcelable