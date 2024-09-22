package com.mshell.discountcalculator.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshell.discountcalculator.core.adapter.FormAdapter
import com.mshell.discountcalculator.core.models.Form
import com.mshell.discountcalculator.databinding.ActivityDiscountFormBinding

class DiscountFormActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityDiscountFormBinding.inflate(layoutInflater)
    }
    private val formAdapter = FormAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar.root as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = "Discal"
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialization()
    }

    private fun initialization() {
        initButton()
        showRecycleList()
    }

    private fun initButton() {
        binding.btnAdd.setOnClickListener {
            formAdapter.addItem(getItem())
        }
    }

    private fun showRecycleList() {
        binding.rvItem.layoutManager = LinearLayoutManager(this)
        binding.rvItem.adapter = formAdapter
        formAdapter.setItemList(getFirstList())
    }

    private fun getFirstList(): MutableList<Form> {
        val list = mutableListOf<Form>()
        for (i in 1..2) {
            list.add(getItem())
        }
        return list
    }

    private fun getItem(): Form {
        return Form(null, null, null)
    }
}