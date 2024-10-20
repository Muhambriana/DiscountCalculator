package com.mshell.discountcalculator.ui.form

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshell.discountcalculator.R
import com.mshell.discountcalculator.core.DiscalViewModelFactory
import com.mshell.discountcalculator.core.adapter.FormAdapter
import com.mshell.discountcalculator.core.data.source.local.CaldisDataSource
import com.mshell.discountcalculator.core.models.DiscountDetail
import com.mshell.discountcalculator.core.models.Form
import com.mshell.discountcalculator.utils.config.DiscountType
import com.mshell.discountcalculator.core.data.DiscalRepository
import com.mshell.discountcalculator.core.data.source.DiscalResource
import com.mshell.discountcalculator.databinding.ActivityDiscountFormBinding
import com.mshell.discountcalculator.ui.itemdetail.ItemDetailBottomFragment
import com.mshell.discountcalculator.utils.config.Config.DEFAULT_DOUBLE_VALUE_ONE
import com.mshell.discountcalculator.utils.helper.Helper
import com.mshell.discountcalculator.utils.helper.Helper.showShortToast
import com.mshell.discountcalculator.utils.view.setSingleClickListener

class DiscountFormActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDiscountFormBinding.inflate(layoutInflater) }
    private val caldisDataSource by lazy { CaldisDataSource() }
    private var discountDetail: DiscountDetail? = null

    private lateinit var formViewModel: DiscountFormViewModel
    private lateinit var formAdapter: FormAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar.root as Toolbar?)
        initialization()
    }

    private fun initialization() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.title = resources.getString(R.string.app_name)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        formViewModel = ViewModelProvider(
            this,
            DiscalViewModelFactory(DiscalRepository(caldisDataSource))
        )[DiscountFormViewModel::class.java]
        formAdapter = FormAdapter()


        setDataToModels()
        viewInitialization()
    }

    @Suppress("DEPRECATION")
    private fun setDataToModels() {
        discountDetail = intent?.extras?.let {
            Helper.returnBasedOnSdkVersion(
                Build.VERSION_CODES.TIRAMISU,
                onSdkEqualOrAbove = {
                    // Return the result from this lambda
                    it.getParcelable(EXTRA_DATA, DiscountDetail::class.java)
                },
                onSdkBelow = {
                    // Return the result from this lambda
                    it.getParcelable(EXTRA_DATA)
                }
            )
        }
    }

    @Suppress("DEPRECATION")
    private fun viewInitialization() {
        initButton()
        supportFragmentManager.setFragmentResultListener(
            ItemDetailBottomFragment.KEY_ADD_ITEM,
            this
        ) { _, bundle ->
            val form: Form? = Helper.returnBasedOnSdkVersion(
                Build.VERSION_CODES.TIRAMISU,
                onSdkEqualOrAbove = {
                    // Return the result from this lambda
                    bundle.getParcelable(EXTRA_DATA_ITEM, Form::class.java)
                },
                onSdkBelow = {
                    // Return the result from this lambda
                    bundle.getParcelable(EXTRA_DATA_ITEM)
                }
            )
            addNewItem(form)
        }
        showRecycleList()
    }

    private fun initButton() {
        binding.btnDeleteItem.setSingleClickListener {

        }
        binding.btnAddItem.setSingleClickListener {
            val bottomDialogFragment = ItemDetailBottomFragment()
            bottomDialogFragment.show(supportFragmentManager, "")
        }
        binding.btnCalculate.setOnClickListener {
            calculate(discountDetail?.discountType)
        }
    }

    private fun addNewItem(form: Form? = null) {
        formViewModel.addNewItem(form)
        formViewModel.item.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is DiscalResource.Loading -> {}
                    is DiscalResource.Empty -> {}
                    is DiscalResource.Error -> {}
                    is DiscalResource.Success -> {
                        formAdapter.addItem(resource.data)
                        changeVisibility(false)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun showRecycleList() {
        binding.rvItemForm.layoutManager = LinearLayoutManager(this)
        binding.rvItemForm.adapter = formAdapter
        formAdapter.onBtnMinusClick = scope@{ model, pos ->
            if ((model.itemQuantity ?: DEFAULT_DOUBLE_VALUE_ONE) <= DEFAULT_DOUBLE_VALUE_ONE) {
                formAdapter.removeItem(model)
                formAdapter.notifyItemRemoved(pos)
                if (formAdapter.itemCount <= 0) changeVisibility(true)
                return@scope
            }
            model.itemQuantity = (model.itemQuantity?.minus(DEFAULT_DOUBLE_VALUE_ONE))
            formAdapter.notifyItemChanged(pos)
        }
        formAdapter.onBtnPlusClick = { model, pos ->
            model.itemQuantity = (model.itemQuantity?.plus(DEFAULT_DOUBLE_VALUE_ONE)) ?: DEFAULT_DOUBLE_VALUE_ONE
            formAdapter.notifyItemChanged(pos)
        }
    }

    private fun changeVisibility(isEmpty: Boolean) {
        if (isEmpty) {
            binding.viewEmpty.root.visibility = View.VISIBLE
            binding.clgNotEmpty.visibility = View.GONE
            binding.viewLoading.root.visibility = View.GONE
            return
        }

        binding.viewEmpty.root.visibility = View.GONE
        binding.clgNotEmpty.visibility = View.VISIBLE
        binding.viewLoading.root.visibility = View.GONE
    }


    private fun calculate(discountType: DiscountType? = null) {
        if (discountType == null) {
            showShortToast("Please choose discount type")
            return
        }
        when (discountType) {
            DiscountType.PERCENT -> calculateDiscountPercent()
            DiscountType.NOMINAL -> calculateDiscountNominal()
        }

    }

    private fun calculateDiscountNominal() {
        formViewModel.getResultDiscountNominal(formAdapter.getList(), discountDetail?.discountNominal)
        observeResult()
    }

    private fun calculateDiscountPercent() {
        formViewModel.getResultDiscountPercent(formAdapter.getList(), discountDetail?.discountPercent?.toDouble(), discountDetail?.discountMax)
        observeResult()
    }


    private fun observeResult() {
        formViewModel.discountResult.observe(this) { event ->
            event.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is DiscalResource.Loading -> {
                        binding.viewLoading.root.visibility = View.VISIBLE
                    }
                    is DiscalResource.Empty -> {
                        binding.viewLoading.root.visibility = View.GONE
                        binding.viewEmpty.root.visibility = View.VISIBLE
                        binding.btnCalculate.visibility = View.GONE
                        binding.btnDeleteItem.visibility = View.GONE
                    }
                    is DiscalResource.Error -> {
                        showShortToast(resource.error?.message.toString())
                        binding.viewLoading.root.visibility = View.GONE
                    }
                    is DiscalResource.Success -> {
                        val list = resource.data
                        formAdapter.notifyAllItem(0, list?.size ?: 0)
                        binding.viewLoading.root.visibility = View.GONE
                    }

                    else -> {}
                }
            }
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_DATA_ITEM = "extra_data_item"
    }
}