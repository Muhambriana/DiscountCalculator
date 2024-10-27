package com.mshell.discountcalculator.core.data.source

sealed class CalDisResource<T>(val data: T? = null, val message: String? = null, val error: Throwable? = null) {
    class Loading<T> : CalDisResource<T>()
    class Success<T>(data: T?) : CalDisResource<T>(data)
    class Error<T>(message: String?, error: Throwable? = null, data: T? = null) : CalDisResource<T>(data, message, error)
    class Empty<T> : CalDisResource<T>()
}
