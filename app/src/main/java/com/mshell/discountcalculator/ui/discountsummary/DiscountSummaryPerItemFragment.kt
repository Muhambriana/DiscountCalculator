package com.mshell.discountcalculator.ui.discountsummary

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.databinding.FragmentDiscountSummaryPerItemBinding
import com.mshell.discountcalculator.utils.helper.Helper
import com.mshell.discountcalculator.utils.helper.Helper.format
import com.mshell.discountcalculator.utils.helper.Helper.toCurrency


private const val EXTRA_DATA = "extra_data"
private const val EXTRA_DATA_SHOPPING_ITEM = "extra_data_shopping_item"

class DiscountSummaryPerItemFragment : BottomSheetDialogFragment() {

    private val binding by lazy {
        FragmentDiscountSummaryPerItemBinding.inflate(layoutInflater)
    }

    private var shoppingDetail: ShoppingDetail? = null
    private var shoppingItem: ShoppingItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        initialization()
    }

    private fun initialization() {
        dataInitialization()
    }

    private fun dataInitialization() {
        arguments?.let {
            @Suppress("DEPRECATION")
            shoppingDetail = Helper.returnBasedOnSdkVersion(
                Build.VERSION_CODES.TIRAMISU,
                onSdkEqualOrAbove = {
                    shoppingItem = it.getParcelable(EXTRA_DATA_SHOPPING_ITEM, ShoppingItem::class.java)
                    it.getParcelable(EXTRA_DATA, ShoppingDetail::class.java)
                },
                onSdkBelow = {
                    shoppingItem = it.getParcelable(EXTRA_DATA_SHOPPING_ITEM)
                    it.getParcelable(EXTRA_DATA)
                }
            )
        }

        setDataToViews()
    }

    @SuppressLint("SetTextI18n")
    private fun setDataToViews() {
        shoppingItem.also {
            with(binding.layoutSummaryDetailPerItem) {
                tvItemName.text = it?.itemName
                tvQuantity.text = "${it?.quantity?.format()} Unit"
                tvPricePerUnit.text = it?.pricePerUnit.toCurrency(2)
                tvTotal.text = it?.totalPrice.toCurrency(2)
                tvTotalDiscount.text = it?.totalDiscount.toCurrency(2)
                tvDiscountPerUnit.text = it?.discountPerUnit.toCurrency(2)
                tvPricePerUnitAfterDiscount.text = it?.pricePerUnitAfterDiscount.toCurrency(2)
                tvTotalPriceAfterDiscount.text = it?.totalPriceAfterDiscount.toCurrency(2)
                tvSummaryDiscount.visibility = View.GONE
                tvSummaryDiscountPerItem.visibility = View.GONE
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(shoppingDetail: ShoppingDetail?, shoppingItem: ShoppingItem?) =
            DiscountSummaryPerItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_DATA, shoppingDetail)
                    putParcelable(EXTRA_DATA_SHOPPING_ITEM, shoppingItem)
                }
            }
    }
}