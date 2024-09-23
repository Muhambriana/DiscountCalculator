package com.mshell.discountcalculator.core.repository

import com.mshell.discountcalculator.core.models.Form

class DiscalRepository {

    fun getItem(): Form {
        return Form(null, null, null)
    }

    fun getFirstList(count: Int): MutableList<Form> {
        val list = mutableListOf<Form>()
        for (i in 1..count) {
            list.add(getItem())
        }
        return list
    }

    fun getDiscountResult() {

    }

}