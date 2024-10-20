package com.mshell.discountcalculator.utils.view

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.mshell.discountcalculator.databinding.DialogCaldisBinding


class CaldisDialog(context: Activity) {
    private val dialog = Dialog(context)
    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val binding by lazy { DialogCaldisBinding.inflate(layoutInflater) }

    // Method to set title and return this
    fun setTitle(title: String): CaldisDialog {
        binding.tvTitle.text = title
        return this
    }

    // Method to set subtitle and return this
    fun setSubtitle(subtitle: String): CaldisDialog {
        binding.tvSubtitle.text = subtitle
        return this
    }

    // Method to set cancel button text and listener, then return this
    fun setBtnNegative(text: String, closeAfterClick: Boolean? = true, listener: View.OnClickListener? = null): CaldisDialog {
        binding.btnNegative.text = text
        binding.btnNegative.setOnClickListener {
            listener?.onClick(it)
            if (closeAfterClick == true) dialog.dismiss()
        }
        return this
    }

    // Method to set yes button text and listener, then return this
    fun setBtnPositive(text: String, closeAfterClick: Boolean? = false, listener: View.OnClickListener? = null): CaldisDialog {
        binding.btnPositive.text = text
        binding.btnPositive.setOnClickListener {
            binding.btnPositive.isEnabled = false
            listener?.onClick(it)
            if (closeAfterClick == true) dialog.dismiss()
        }
        return this
    }

    fun hideTitle(): CaldisDialog {
        binding.tvTitle.visibility = View.GONE
        return this
    }

    fun hideSubtitle(): CaldisDialog {
        binding.tvSubtitle.visibility = View.GONE
        return this
    }

    fun showOnlyButtonYes(): CaldisDialog {
        binding.btnNegative.visibility = View.GONE
        binding.btnPositive.visibility = View.VISIBLE
        return this
    }

    fun showOnlyButtonNo(): CaldisDialog {
        binding.btnNegative.visibility = View.VISIBLE
        binding.btnPositive.visibility = View.GONE
        return this
    }

    fun setOutsideTouchable(condition: Boolean): CaldisDialog {
        dialog.setCancelable(condition)
        dialog.setCanceledOnTouchOutside(condition)
        return this
    }

    fun show(): CaldisDialog {
        try {
            val width = WindowManager.LayoutParams.MATCH_PARENT // or a specific pixel size
            val height = WindowManager.LayoutParams.WRAP_CONTENT // or a specific pixel size
            dialog.setContentView(binding.root)
            dialog.window?.apply {
                setLayout(width, height)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }
}