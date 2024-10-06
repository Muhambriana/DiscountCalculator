package com.mshell.discountcalculator.utils.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class CurrencyEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : androidx.appcompat.widget.AppCompatEditText(context, attrs, defStyleAttr), TextWatcher {

    init {
        addTextChangedListener(this) // Add the TextWatcher to this EditText
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        // No action needed before text changes
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        // No action needed during text changes
    }

    override fun afterTextChanged(editable: Editable) {
        // Prevent recursive calls
        removeTextChangedListener(this)

        // Track the cursor position
        val cursorPosition = selectionStart
        val value = editable.toString()

        // Prevent leading zeros
        if (value.isNotEmpty() && value[0] == '0' && value.length > 1) {
            // If the first character is zero and there are more characters, remove the zero
            setText(value.substring(1))
            setSelection(cursorPosition - 1) // Adjust the cursor position
            addTextChangedListener(this)
            return // Exit early to prevent further processing
        }

        if (value.isNotEmpty()) {
            // Handle leading zeros and decimal points
            var formattedValue = value.replace(".", "") // Remove existing dots for formatting
            if (formattedValue.startsWith(".")) {
                formattedValue = "0$formattedValue"
            } else if (formattedValue.startsWith("0") && !formattedValue.startsWith("0.")) {
                formattedValue = "0"
            }

            // Format the number with dots as thousands separators
            formattedValue = formatWithDots(formattedValue)

            // Update the text and restore cursor position
            setText(formattedValue)

            // Adjust the cursor position based on the change
            val newCursorPosition = calculateCursorPosition(value, formattedValue, cursorPosition)
            setSelection(newCursorPosition)
        }

        // Re-add the listener
        addTextChangedListener(this)
    }

    private fun calculateCursorPosition(oldValue: String, newValue: String, oldCursorPosition: Int): Int {
        // Calculate the new cursor position after formatting
        val difference = newValue.length - oldValue.length
        return (oldCursorPosition + difference).coerceIn(0, newValue.length)
    }

    private fun formatWithDots(value: String): String {
        return try {
            // Parse the number and format it with dots as thousands separators
            val decimalFormat = DecimalFormat("#,###").apply {
                decimalFormatSymbols = DecimalFormatSymbols(Locale("id", "ID")).apply {
                    groupingSeparator = '.'
                    decimalSeparator = ','
                }
            }
            val number = value.toDoubleOrNull() ?: 0.0
            decimalFormat.format(number)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            value // Return the original value if parsing fails
        }
    }
}

