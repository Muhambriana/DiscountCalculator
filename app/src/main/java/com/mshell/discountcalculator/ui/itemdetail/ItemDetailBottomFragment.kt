package com.mshell.discountcalculator.ui.itemdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mshell.discountcalculator.core.DiscalViewModelFactory
import com.mshell.discountcalculator.core.repository.DiscalRepository
import com.mshell.discountcalculator.core.resource.DiscalResource
import com.mshell.discountcalculator.databinding.FragmentItemDetailBottomBinding
import com.mshell.discountcalculator.ui.form.DiscountFormActivity
import com.mshell.discountcalculator.utils.view.setSingleClickListener

class ItemDetailBottomFragment : BottomSheetDialogFragment() {

    private val binding by lazy {
        FragmentItemDetailBottomBinding.inflate(layoutInflater)
    }

    private lateinit var itemDetailViewModel: ItemDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemDetailViewModel = ViewModelProvider(
            this,
            DiscalViewModelFactory(DiscalRepository())
        )[ItemDetailViewModel::class.java]
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
                    is DiscalResource.Loading -> {
                        binding.viewLoading.root.visibility = View.VISIBLE
                    }

                    is DiscalResource.Empty -> {}
                    is DiscalResource.Error -> {
                        binding.viewLoading.root.visibility = View.GONE
                        Log.d(tag, resource.error?.message.toString())
                        resource.error?.printStackTrace()
                    }

                    is DiscalResource.Success -> {
                        val bundle = Bundle()
                        bundle.apply {
                            putParcelable(DiscountFormActivity.EXTRA_DATA_ITEM, resource.data)
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