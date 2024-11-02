package com.mshell.discountcalculator.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.mshell.discountcalculator.R
import com.mshell.discountcalculator.core.data.source.local.CalDisDataSource
import com.mshell.discountcalculator.core.data.source.CalDisResource
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.databinding.ActivityHomeBinding
import com.mshell.discountcalculator.ui.shoppinglist.ShoppingItemListActivity
import com.mshell.discountcalculator.utils.config.DiscountType
import com.mshell.discountcalculator.utils.helper.Helper
import com.mshell.discountcalculator.utils.view.setSingleClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private val thisContext = this
    private val tag = this.javaClass.simpleName
    private val binding by lazy { ActivityHomeBinding.inflate(layoutInflater) }
    private val homeViewModel: HomeViewModel by viewModel()


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
        binding.btnNext.setSingleClickListener {
            getDiscountData()
        }
    }

    private fun getDiscountData() {
        homeViewModel.getShoppingDetail(binding)
        homeViewModel.shoppingDetail.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is CalDisResource.Loading -> {
                        binding.viewLoading.root.visibility = View.VISIBLE
                    }

                    is CalDisResource.Empty -> {}
                    is CalDisResource.Error -> {
                        binding.viewLoading.root.visibility = View.GONE
                        Log.d(tag, resource.error?.message.toString())
                        resource.error?.printStackTrace()
                    }

                    is CalDisResource.Success -> {
                        moveIntent(resource.data)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun moveIntent(shoppingDetail: ShoppingDetail?) {
        val intent = Intent(this, ShoppingItemListActivity::class.java)
        intent.putExtra(ShoppingItemListActivity.EXTRA_DATA, shoppingDetail)
        startActivity(intent)
        binding.viewLoading.root.visibility = View.GONE
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
        binding.clgDetailDiscount.also {
            if (it.isVisible) return
            it.visibility = View.VISIBLE
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
        binding.optionPercentDiscount.background = Helper.setShapeDrawableStrokeColor(
            resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._2sdp), // Dynamic stroke width
            ContextCompat.getColor(this, getColor(isChecked)), // Dynamic stroke color
            resources.getDimension(com.intuit.sdp.R.dimen._6sdp), // Dynamic corner radius
        )
        binding.layoutFormDiscountPercent.root.visibility =
            if (isChecked) View.VISIBLE else View.GONE
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
        binding.layoutFormDiscountNominal.root.visibility =
            if (isChecked) View.VISIBLE else View.GONE
    }

    private fun getColor(flag: Boolean): Int {
        return if (flag) R.color.my_light_primary else R.color.adaptive_primary_color
    }
}