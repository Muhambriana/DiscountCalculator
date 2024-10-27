package com.mshell.discountcalculator.core

class CalDisEvent<out T> (private val content: T) {
    private var hasBeenHandled = false

    // If want to use one time live data, its prevent to use again
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        }
        else {
            hasBeenHandled = true
            content
        }
    }

    // Alternative if want to use normally live data
    fun peekContent(): T = content
}