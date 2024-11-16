package com.mshell.discountcalculator.core.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoppingDetail(

    var shoppingId: Long = 0,

    var discountDetail: DiscountDetail,

    var totalShopping: Double = 0.0,

    var additional: Double = 0.0,

    var total: Double = 0.0,

    var totalQuantity: Double = 0.0,

    var discount: Double = 0.0,

    var totalAfterDiscount: Double = 0.0,

    var listItem: MutableList<ShoppingItem> = mutableListOf()

) : Parcelable