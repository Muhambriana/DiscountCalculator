package com.mshell.discountcalculator.core.resource

sealed class DiscalResource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T> :DiscalResource<T>()
    class Success<T>(data: T?) : DiscalResource<T>(data)
    class Error<T>(message: String?, data: T? = null) : DiscalResource<T>(data, message)
    class Empty<T> : DiscalResource<T>()
}
