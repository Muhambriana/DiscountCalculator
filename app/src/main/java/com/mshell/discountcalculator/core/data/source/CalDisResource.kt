package com.mshell.discountcalculator.core.data.source

import com.mshell.discountcalculator.utils.config.ExceptionTypeEnum

sealed class CalDisResource<T>(val data: T? = null, val message: String? = null, val error: Throwable? = null, val exceptionTypeEnum: ExceptionTypeEnum? = null) {
    class Loading<T> : CalDisResource<T>()
    class Success<T>(data: T?) : CalDisResource<T>(data)
    class Error<T>(message: String?, error: Throwable? = null, exceptionTypeEnum: ExceptionTypeEnum? = null,data: T? = null) : CalDisResource<T>(data, message, error, exceptionTypeEnum)
    class Empty<T> : CalDisResource<T>()
}
