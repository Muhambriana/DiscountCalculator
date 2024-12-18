package com.mshell.discountcalculator.utils.helper

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

object Helper {
    fun Activity.showLongToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun Activity.showShortToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun setColorVectorDrawable(context: Context, drawableResId: Int, colorResId: Int): Drawable? {
        // Load the vector drawable
        val drawable: Drawable? = AppCompatResources.getDrawable(context, drawableResId)

        // Tint the drawable with the specified color
        drawable?.let {
            val color = context.getColor(colorResId) // Get the color from resources
            DrawableCompat.setTint(it, color) // Apply the tint
        }

        return drawable // Return the tinted drawable
    }

    private fun setBackgroundDrawable(
        context: Context,
        drawableResId: Int,
        colorResId: Int
    ):GradientDrawable  {
        val drawableResult = ContextCompat.getDrawable(context, drawableResId)
            ?.mutate() as GradientDrawable

        drawableResult.color = ColorStateList.valueOf(colorResId)
        return drawableResult
    }

    fun setShapeDrawableStrokeColor(width: Int, color: Int, cornerRadius: Float): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setStroke(width, color)
            this.cornerRadius = cornerRadius
        }
    }
}