package com.mshell.discountcalculator.ui.discountsummary

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshell.discountcalculator.core.adapter.ShoppingItemAdapter
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.databinding.FragmentDiscountSummaryBinding
import com.mshell.discountcalculator.utils.config.Config
import com.mshell.discountcalculator.utils.config.DiscountType
import com.mshell.discountcalculator.utils.helper.Helper
import com.mshell.discountcalculator.utils.helper.Helper.toCurrency

private const val EXTRA_DATA_SHOPPING_DETAIL = "extra_data_shopping_detail"

class DiscountSummaryFragment : Fragment() {

    private val binding by lazy {
        FragmentDiscountSummaryBinding.inflate(layoutInflater)
    }

    private val shoppingItemAdapter by lazy {
        ShoppingItemAdapter(true)
    }

    private var listItem: MutableList<ShoppingItem>? = null
    private var shoppingDetail: ShoppingDetail? = null

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
        setUpData()
    }

    private fun setUpData() {
        arguments?.let {
            @Suppress("DEPRECATION")
            shoppingDetail = Helper.returnBasedOnSdkVersion(
                Build.VERSION_CODES.TIRAMISU,
                onSdkEqualOrAbove = {
                   it.getParcelable(EXTRA_DATA_SHOPPING_DETAIL, ShoppingDetail::class.java)
                },
                onSdkBelow = {
                    it.getParcelable(EXTRA_DATA_SHOPPING_DETAIL)
                }
            )
            listItem = shoppingDetail?.listItem
        }

        showRecyclerView()
        setDataValueToView()
    }

    private fun setDataValueToView() {
        shoppingDetail.also {
            with(binding.layoutSummaryDetail) {
                tvTotalShopping.text = it?.totalShopping.toCurrency(2)
                tvAdditional.text = it?.additional.toCurrency(2)
                tvTotal.text = it?.total.toCurrency(2)
                tvDiscount.text = it?.discount.toCurrency()
                tvAfterDiscount.text = it?.totalAfterDiscount.toCurrency(2)
                tvSummaryDiscount.text = it?.let {
                    when(it.discountDetail.discountType) {
                        DiscountType.NOMINAL -> {
                            "${it.total.toCurrency(2)} - ${it.discountDetail.discountNominal.toCurrency(2)}"
                        }
                        DiscountType.PERCENT -> {
                            "${it.total.toCurrency(2)} X ${it.discountDetail.discountPercent}%" +
                                    "\n" +
                                    "Max: ${it.discountDetail.discountMax?.toCurrency(2)}"
                        }
                        else -> Config.EMPTY_STRING
                    }
                }
            }
        }
    }

    private fun showRecyclerView() {
        shoppingItemAdapter.setItemList(listItem)
        shoppingItemAdapter.onItemClick = {
            openSummaryPerItemFragment(it)
        }
        binding.rvItemShopping.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = shoppingItemAdapter
        }
    }

    private fun openSummaryPerItemFragment(shoppingItem: ShoppingItem) {
        val bottomDialogFragment = DiscountSummaryPerItemFragment.newInstance(shoppingDetail, shoppingItem)
        activity?.supportFragmentManager?.also { bottomDialogFragment.show(it, bottomDialogFragment.tag) }
    }

    companion object {
        @JvmStatic
        fun newInstance(shoppingDetail: ShoppingDetail?) =
            DiscountSummaryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_DATA_SHOPPING_DETAIL, shoppingDetail)
                }
            }
    }
}