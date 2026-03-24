package com.mshell.discalc.core.models

import android.os.Parcelable
import com.mshell.discalc.utils.config.DiscountType
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountDetail(

    var discountType: DiscountType? = null,

    var discountNominal: Double? = null,

    var discountPercent: Int? = null,

    var discountMax: Double? = null,

) : Parcelable
