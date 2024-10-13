package com.mshell.discountcalculator.core.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Form(
    var itemName: String? = null,

    var itemPrice: Double? = null,

    var itemQuantity: Double? = null,

    var total: Double? = null,

    var itemDiscount: Double? = null,

    var afterDiscount: Double? = null
) : Parcelable
