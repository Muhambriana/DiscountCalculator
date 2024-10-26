package com.mshell.discountcalculator.ui.discountsummary

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshell.discountcalculator.core.DiscalViewModelFactory
import com.mshell.discountcalculator.core.adapter.ShoppingItemAdapter
import com.mshell.discountcalculator.core.data.DiscalRepository
import com.mshell.discountcalculator.core.data.source.local.CaldisDataSource
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.databinding.FragmentDiscountSummaryBinding
import com.mshell.discountcalculator.utils.config.Config
import com.mshell.discountcalculator.utils.config.DiscountType
import com.mshell.discountcalculator.utils.helper.Helper
import com.mshell.discountcalculator.utils.helper.Helper.toCurrency

private const val EXTRA_DATA_LIST = "extra_data_list"
private const val EXTRA_DATA_DISCOUNT_DETAIL = "extra_data_discount_detail"

class DiscountSummaryFragment : Fragment() {

    private val binding by lazy {
        FragmentDiscountSummaryBinding.inflate(layoutInflater)
    }

    private val caldisDataSource by lazy {
        CaldisDataSource()
    }

    private val summaryViewModel by lazy {
        ViewModelProvider(
            this,
            DiscalViewModelFactory(DiscalRepository(caldisDataSource))
        )[DiscountSummaryViewModel::class.java]
    }

    private val shoppingItemAdapter by lazy {
        ShoppingItemAdapter(true)
    }

    private var listItem: MutableList<ShoppingItem>? = null
    private var shoppingDetail: ShoppingDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataInitialization()
        viewInitialization()
    }

    private fun dataInitialization() {
        arguments?.let {
            @Suppress("DEPRECATION")
            Helper.executeBasedOnSdkVersion(
                Build.VERSION_CODES.TIRAMISU,
                onSdkEqualOrAbove = {
                    shoppingDetail = it.getParcelable(EXTRA_DATA_DISCOUNT_DETAIL, ShoppingDetail::class.java)
                    listItem = it.getParcelableArrayList(EXTRA_DATA_LIST, ShoppingItem::class.java)
                },
                onSdkBelow = {
                    shoppingDetail = it.getParcelable(EXTRA_DATA_DISCOUNT_DETAIL)
                    listItem = it.getParcelableArrayList(EXTRA_DATA_LIST)
                }
            )
        }
    }

    private fun viewInitialization() {
        showRecyclerView()
        shoppingDetail.also {
            with(binding.layoutSummaryDetail) {
                tvTotalShopping.text = it?.totalShopping.toCurrency(2)
                tvAdditional.text = it?.additional.toCurrency(2)
                tvTotal.text = it?.total.toCurrency(2)
                tvDiscount.text = it?.discount.toCurrency()
                tvAfterDiscount.text = it?.totalAfterDiscount.toCurrency(2)
                tvSummaryDiscount.text = it?.let {
                    when(it.discountDetail?.discountType) {
                        DiscountType.NOMINAL -> {
                            "${it.total.toCurrency(2)} - ${it.discountDetail?.discountNominal.toCurrency(2)}"
                        }
                        DiscountType.PERCENT -> {
                            "${it.total.toCurrency(2)} X ${it.discountDetail?.discountPercent}%" +
                                    "\n" +
                                    "Max: ${it.discountDetail?.discountMax?.toCurrency(2)}"
                        }
                        else -> Config.EMPTY_STRING
                    }
                }
            }
        }
    }

    private fun showRecyclerView() {
        shoppingItemAdapter.setItemList(listItem)
        binding.rvItemShopping.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = shoppingItemAdapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(list: ArrayList<ShoppingItem>? = null, discountDetail: ShoppingDetail?) =
            DiscountSummaryFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(EXTRA_DATA_LIST, list)
                    putParcelable(EXTRA_DATA_DISCOUNT_DETAIL, discountDetail)
                }
            }
    }
}