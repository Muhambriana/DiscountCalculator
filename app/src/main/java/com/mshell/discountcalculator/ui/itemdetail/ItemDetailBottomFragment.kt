package com.mshell.discountcalculator.ui.itemdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mshell.discountcalculator.core.CalDisViewModelFactory
import com.mshell.discountcalculator.core.data.source.local.CalDisDataSource
import com.mshell.discountcalculator.core.data.CalDisRepository
import com.mshell.discountcalculator.core.data.source.CalDisResource
import com.mshell.discountcalculator.databinding.FragmentItemDetailBottomBinding
import com.mshell.discountcalculator.ui.shoppinglist.ShoppingItemListActivity
import com.mshell.discountcalculator.utils.view.setSingleClickListener

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
        viewInitialization()
    }

    private fun viewInitialization() {
        binding.btnCancel.setSingleClickListener {
            dismiss()
        }

        binding.btnNext.setSingleClickListener {
            getItemDetail()
        }
    }

    private fun getItemDetail() {
        itemDetailViewModel.getItemDetail(binding)
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
                        val bundle = Bundle()
                        bundle.apply {
                            putParcelable(ShoppingItemListActivity.EXTRA_DATA_ITEM, resource.data)
                        }
                        setFragmentResult(KEY_ADD_ITEM, bundle)
                        dismiss()
                    }

                    else -> {}
                }
            }
        }
    }

    companion object {
        const val KEY_ADD_ITEM = "key_add_item"
    }
}