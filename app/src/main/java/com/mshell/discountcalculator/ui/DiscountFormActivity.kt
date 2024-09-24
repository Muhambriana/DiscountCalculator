package com.mshell.discountcalculator.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshell.discountcalculator.core.DiscalViewModelFactory
import com.mshell.discountcalculator.core.adapter.FormAdapter
import com.mshell.discountcalculator.core.enumclass.DiscountType
import com.mshell.discountcalculator.core.repository.DiscalRepository
import com.mshell.discountcalculator.core.resource.DiscalResource
import com.mshell.discountcalculator.databinding.ActivityDiscountFormBinding

class DiscountFormActivity : AppCompatActivity() {

    private lateinit var formViewModel: DiscountFormViewModel
    private lateinit var formAdapter: FormAdapter

    private val binding by lazy {
        ActivityDiscountFormBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar.root as Toolbar?)
        initialization()
    }

    private fun initialization() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = "Discal"
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        formViewModel = ViewModelProvider(
            this,
            DiscalViewModelFactory(DiscalRepository())
        )[DiscountFormViewModel::class.java]
        formAdapter = FormAdapter()

        initButton()
        getItemList()
    }

    private fun initButton() {
        binding.btnCalculate.setOnClickListener {
            calculate()
        }
    }

    private fun getItem() {
        formViewModel.getItem()
        formViewModel.resourceItemForm.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is DiscalResource.Loading -> {}
                    is DiscalResource.Empty -> {}
                    is DiscalResource.Error -> {}
                    is DiscalResource.Success -> {
                        formAdapter.addItem(resource.data)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showRecycleList() {
        binding.rvItem.layoutManager = LinearLayoutManager(this)
        binding.rvItem.adapter = formAdapter
    }

    private fun getItemList() {
        formViewModel.getFirstList(2)
        formViewModel.resourceItemsForm.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is DiscalResource.Loading -> {
                        binding.viewLoading.root.visibility = View.VISIBLE
                    }
                    is DiscalResource.Empty -> {}
                    is DiscalResource.Error -> {}
                    is DiscalResource.Success -> {
                        formAdapter.setItemList(resource.data)
                        showRecycleList()
                        binding.viewLoading.root.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }
    }


    private fun calculate(tempParam: Int? = null) {
        val discountType = if (tempParam == null) DiscountType.PERCENT else DiscountType.NOMINAL


        when(discountType) {
            DiscountType.PERCENT -> calculateDiscountPercent()
            DiscountType.NOMINAL -> calculateDiscountNominal()
        }

    }

    private fun calculateDiscountNominal() {
        val discount = binding.edDiscountNominal.text.toString().toDouble()
        formViewModel.getResultDiscountNominal(formAdapter.getList(), discount)
        observeResult()
    }

    private fun calculateDiscountPercent() {
        val discount = binding.edDiscount.text.toString().toDouble()
        val maxDiscount = binding.edMaxDiscount.text.toString().toDouble()
        formViewModel.getResultDiscountPercent(formAdapter.getList(), discount, maxDiscount)
        observeResult()
    }



    private fun observeResult() {
        formViewModel.discountResult.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when(resource) {
                    is DiscalResource.Loading -> {
                        binding.viewLoading.root.visibility = View.VISIBLE
                    }
                    is DiscalResource.Empty -> {}
                    is DiscalResource.Error -> {}
                    is DiscalResource.Success -> {
                        val list = resource.data
                        binding.tvResult.text = list?.joinToString("\n") { it.discount.toString() }
                        binding.viewLoading.root.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }
    }

}