package com.mshell.discountcalculator.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mshell.discountcalculator.R
import com.mshell.discountcalculator.databinding.ActivityHomeBinding
import com.mshell.discountcalculator.utils.config.DiscountType
import com.mshell.discountcalculator.utils.helper.Helper

class HomeActivity : AppCompatActivity() {

    private val thisContext = this
    private val tag = this.javaClass.simpleName

    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar.root as Toolbar?)
        initialization()
    }

    private fun initialization() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = resources.getString(R.string.app_name)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewInitialization()
    }

    private fun viewInitialization() {
        binding.radioGroupDiscount.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbPercent.id -> {
                    stateChecked(DiscountType.PERCENT)
                }

                binding.rbNominal.id -> {
                    stateChecked(DiscountType.NOMINAL)
                }
            }
        }
    }

    private fun stateChecked(discountType: DiscountType) {
        when (discountType) {
            DiscountType.PERCENT -> {
                percent(true)
                nominal(false)
            }

            DiscountType.NOMINAL -> {
                percent(false)
                nominal(true)
            }
        }
    }

    private fun percent(isChecked: Boolean) {
        binding.ivIconPercent.setImageDrawable(
            Helper.setColorVectorDrawable(
                thisContext,
                R.drawable.ic_discount_black,
                getColor(isChecked)
            )
        )
        binding.optionNominalDiscount.background = Helper.setShapeDrawableStrokeColor(
            resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._2sdp), // Dynamic stroke width
            ContextCompat.getColor(this, getColor(isChecked)), // Dynamic stroke color
            resources.getDimension(com.intuit.sdp.R.dimen._6sdp), // Dynamic corner radius
        )
    }

    private fun nominal(isChecked: Boolean) {
        binding.ivIconNominal.setImageDrawable(
            Helper.setColorVectorDrawable(
                thisContext,
                R.drawable.ic_wallet_black,
                getColor(isChecked)
            )
        )
        binding.optionNominalDiscount.background = Helper.setShapeDrawableStrokeColor(
            resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._2sdp), // Dynamic stroke width
            ContextCompat.getColor(this, getColor(isChecked)), // Dynamic stroke color
            resources.getDimension(com.intuit.sdp.R.dimen._6sdp), // Dynamic corner radius
        )
    }

    private fun getColor(flag: Boolean): Int {
        return if (flag) R.color.my_light_primary else R.color.black
    }
}