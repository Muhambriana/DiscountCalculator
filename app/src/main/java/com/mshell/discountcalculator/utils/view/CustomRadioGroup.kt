package com.mshell.discountcalculator.utils.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import android.widget.LinearLayout

class CustomRadioGroup : LinearLayout {
    private val listCheckable = ArrayList<View>()
    private var onCheckedChangeListener: ((CustomRadioGroup, Int) -> Unit)? = null
    private var checkedId: Int = View.NO_ID

    constructor(context: Context?) : super(context)

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int = 0) : super(
        context,
        attrs,
        defStyle
    )

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        parseChild(child)
    }

    private fun parseChild(child: View) {
        if (child is Checkable) {
            listCheckable.add(child)
            child.setOnClickListener { v ->
                for (i in listCheckable.indices) {
                    val view = listCheckable[i] as Checkable
                    if (view === v) {
                        view.isChecked = true
                        checkedId = v.id
                        onCheckedChangeListener?.invoke(this, checkedId) // Use lambda
                    } else {
                        view.isChecked = false
                    }
                }
            }
        } else if (child is ViewGroup) {
            parseChildren(child)
        }
    }

    private fun parseChildren(child: ViewGroup) {
        for (i in 0 until child.childCount) {
            parseChild(child.getChildAt(i))
        }
    }

    // Updated method to accept a lambda
    fun setOnCheckedChangeListener(listener: (CustomRadioGroup, Int) -> Unit) {
        onCheckedChangeListener = listener
    }
}



