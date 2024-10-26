package com.mshell.discountcalculator.ui.discountresult

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshell.discountcalculator.core.adapter.FormAdapter
import com.mshell.discountcalculator.core.models.Form
import com.mshell.discountcalculator.databinding.FragmentDiscountResultBinding
import com.mshell.discountcalculator.utils.helper.Helper

private const val EXTRA_LIST_DATA = "extra_list_data"

class DiscountResultFragment : Fragment() {

    private val binding by lazy {
        FragmentDiscountResultBinding.inflate(layoutInflater)
    }

    private val formAdapter by lazy {
        FormAdapter(true)
    }

    // TODO: Rename and change types of parameters
    private var listItem: MutableList<Form>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataInitialization()
        viewInitialization()
    }

    private fun dataInitialization() {
        arguments?.let {
            listItem = it.let {
                Helper.returnBasedOnSdkVersion(
                    Build.VERSION_CODES.TIRAMISU,
                    onSdkEqualOrAbove = {
                        // Return the result from this lambda
                        it.getParcelableArrayList(EXTRA_LIST_DATA, Form::class.java)
                    },
                    onSdkBelow = {
                        // Return the result from this lambda
                        @Suppress("DEPRECATION")
                        it.getParcelableArrayList(EXTRA_LIST_DATA)
                    }
                )
            }
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
        fun newInstance(list: ArrayList<Form>? = null) =
            DiscountResultFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(EXTRA_LIST_DATA, list)
                }
            }
    }
}