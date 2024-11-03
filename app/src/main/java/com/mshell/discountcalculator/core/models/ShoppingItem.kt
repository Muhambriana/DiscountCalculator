package com.mshell.discountcalculator.core.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoppingItem(

    var id: Long? = 0,

    var itemName: String? = null,

    var pricePerUnit: Double? = null,

    var quantity: Double? = null,

    var totalPrice: Double? = null,

    var totalDiscount: Double? = null,

    var discountPerUnit: Double? = null,

    var pricePerUnitAfterDiscount: Double? = null,

    var totalPriceAfterDiscount: Double? = null
) : Parcelable
