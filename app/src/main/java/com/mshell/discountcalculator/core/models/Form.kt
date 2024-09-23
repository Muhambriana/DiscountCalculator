package com.mshell.discountcalculator.core.models

data class Form(

    var itemName: String? = null,

    var itemPrice: Double? = null,

    var itemQuantity: Double? = null,

    var total: Double? = null,

    var discount: Double? = null,

    var afterDiscount: Double? = null
)
