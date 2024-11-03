package com.mshell.discountcalculator.utils.config

enum class ExceptionTypeEnum(val code: Int, val key: String, val defaultMessage: String?) {
    OK(0, "error.ok", null as String?),

    NULL_FIELD(801, "null field", null as String?),

    EMPTY_FORM(802, "Form can't be empty", null as String?),

    RESULT_EMPTY(803, "Result is empty", null as String?),

    RESULT_ERROR(804, "Error from view model", null as String?),

    RESULT_ERROR_2(805, "Error from repository", null as String?),

    UNEXPECTED_ERROR(-9999, "error.unexpected", "Unexpected error");

    val codeAsString: String
        get() = "Code: $code"
}