package com.mshell.discountcalculator.core.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoppingItem(

    var id: Long = 0,

    var shoppingId: Long = 0,

    var itemName: String? = null,

    var pricePerUnit: Double = 0.0,

    var quantity: Double = 1.0,

    var totalPrice: Double = 0.0,

    var totalDiscount: Double = 0.0,

    var discountPerUnit: Double = 0.0,

    var pricePerUnitAfterDiscount: Double = 0.0,

    var totalPriceAfterDiscount: Double = 0.0
) : Parcelable
