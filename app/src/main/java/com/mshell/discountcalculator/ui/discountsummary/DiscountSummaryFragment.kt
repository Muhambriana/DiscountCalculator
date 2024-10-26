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
import com.mshell.discountcalculator.core.adapter.FormAdapter
import com.mshell.discountcalculator.core.data.DiscalRepository
import com.mshell.discountcalculator.core.data.source.local.CaldisDataSource
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.databinding.FragmentDiscountSummaryBinding
import com.mshell.discountcalculator.utils.helper.Helper

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

    private val formAdapter by lazy {
        FormAdapter(true)
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
    }

    private fun showRecyclerView() {
        formAdapter.setItemList(listItem)
        binding.rvItemForm.apply {
            layoutManager = LinearLayoutManager(activity)
            binding.rvItemForm.adapter = formAdapter
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