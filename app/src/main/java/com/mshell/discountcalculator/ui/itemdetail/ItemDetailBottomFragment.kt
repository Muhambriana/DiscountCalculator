package com.mshell.discountcalculator.ui.itemdetail

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mshell.discountcalculator.R
import com.mshell.discountcalculator.core.CalDisViewModelFactory
import com.mshell.discountcalculator.core.data.source.local.CalDisDataSource
import com.mshell.discountcalculator.core.data.CalDisRepository
import com.mshell.discountcalculator.core.data.source.CalDisResource
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.databinding.FragmentItemDetailBottomBinding
import com.mshell.discountcalculator.ui.shoppinglist.ShoppingItemListActivity
import com.mshell.discountcalculator.utils.helper.Helper
import com.mshell.discountcalculator.utils.helper.Helper.toCurrency
import com.mshell.discountcalculator.utils.view.setSingleClickListener

private const val EXTRA_DATA = "extra_id"

class ItemDetailBottomFragment : BottomSheetDialogFragment() {

    private val binding by lazy {
        FragmentItemDetailBottomBinding.inflate(layoutInflater)
    }

    private val caldisDataSource by lazy {
        CalDisDataSource()
    }

    private val itemDetailViewModel by lazy {
        ViewModelProvider(
            this,
            CalDisViewModelFactory(CalDisRepository(caldisDataSource))
        )[ItemDetailViewModel::class.java]
    }

    private var shoppingItem: ShoppingItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        setUpView()
    }

    private fun setUpData() {
        arguments?.let {
            @Suppress("DEPRECATION")
            shoppingItem = Helper.returnBasedOnSdkVersion(
                Build.VERSION_CODES.TIRAMISU,
                onSdkEqualOrAbove = {
                    it.getParcelable(EXTRA_DATA, ShoppingItem::class.java)
                },
                onSdkBelow = {
                    it.getParcelable(EXTRA_DATA)
                }
            )
        }

        if (shoppingItem == null) return

        binding.tvTitle.text = getString(R.string.edit_item)
        binding.edItemName.setText(shoppingItem?.itemName.toString())
        binding.edPricePerUnit.edCurrency.setText(shoppingItem?.pricePerUnit.toCurrency(outputNumbersOnly = true))
    }

    private fun setUpView() {
        binding.btnCancel.setSingleClickListener {
            dismiss()
        }

        binding.btnNext.setSingleClickListener {
            getItemDetail()
        }
    }

    private fun getItemDetail() {
        itemDetailViewModel.getItemDetail(binding, shoppingItem ?: ShoppingItem())
        itemDetailViewModel.itemDetail.observe(this) { event ->
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
                        val bundle = Bundle().apply {
                            putParcelable(ShoppingItemListActivity.EXTRA_DATA_ITEM, resource.data)
                        }
                        setFragmentResult(
                            if (shoppingItem== null) KEY_ADD_ITEM else KEY_UPDATE_ITEM,
                            bundle
                        )
                        dismiss()
                    }

                    else -> {}
                }
            }
        }
    }

    companion object {
        const val KEY_ADD_ITEM = "key_add_item"
        const val KEY_UPDATE_ITEM = "key_update_item"
        @JvmStatic
        fun newInstance(shoppingItem: ShoppingItem?) =
            ItemDetailBottomFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_DATA, shoppingItem)
                }
            }
    }
}