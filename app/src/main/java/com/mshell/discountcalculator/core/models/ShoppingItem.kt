package com.mshell.discountcalculator.core.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoppingItem(

    var itemId: Int? = 0,

    var itemName: String? = null,

    var itemPrice: Double? = null,

    var itemQuantity: Double? = null,

    var totalPrice: Double? = null,

    var totalDiscount: Double? = null,

    var discountPerItem: Double? = null,

    var pricePerItemAfterDiscount: Double? = null,

    var totalAfterDiscount: Double? = null
) : Parcelable
