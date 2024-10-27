package com.mshell.discountcalculator.ui.shoppinglist

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshell.discountcalculator.R
import com.mshell.discountcalculator.core.DiscalViewModelFactory
import com.mshell.discountcalculator.core.adapter.ShoppingItemAdapter
import com.mshell.discountcalculator.core.data.DiscalRepository
import com.mshell.discountcalculator.core.data.source.DiscalResource
import com.mshell.discountcalculator.core.data.source.local.CaldisDataSource
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.databinding.ActivityDiscountFormBinding
import com.mshell.discountcalculator.ui.discountsummary.DiscountSummaryFragment
import com.mshell.discountcalculator.ui.itemdetail.ItemDetailBottomFragment
import com.mshell.discountcalculator.utils.config.Config
import com.mshell.discountcalculator.utils.helper.Helper
import com.mshell.discountcalculator.utils.helper.Helper.observeOnce
import com.mshell.discountcalculator.utils.helper.Helper.showShortToast
import com.mshell.discountcalculator.utils.view.CaldisDialog
import com.mshell.discountcalculator.utils.view.setSingleClickListener


class ShoppingItemListActivity : AppCompatActivity() {

    private val thisActivityContext by lazy {
        this
    }
    private val binding by lazy {
        ActivityDiscountFormBinding.inflate(layoutInflater)
    }

    private val caldisDataSource by lazy {
        CaldisDataSource()
    }

    private val formViewModel by lazy {
        ViewModelProvider(
            this,
            DiscalViewModelFactory(DiscalRepository(caldisDataSource))
        )[ShoppingItemListViewModel::class.java]
    }

    private val shoppingItemAdapter by lazy {
        ShoppingItemAdapter(false)
    }

    private var shoppingDetail: ShoppingDetail? = null


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

        setDataToModels()
        viewInitialization()
    }

    @Suppress("DEPRECATION")
    private fun setDataToModels() {
        shoppingDetail = intent?.extras?.let {
            Helper.returnBasedOnSdkVersion(
                Build.VERSION_CODES.TIRAMISU,
                onSdkEqualOrAbove = {
                    // Return the result from this lambda
                    it.getParcelable(EXTRA_DATA, ShoppingDetail::class.java)
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
            val shoppingItem: ShoppingItem? = Helper.returnBasedOnSdkVersion(
                Build.VERSION_CODES.TIRAMISU,
                onSdkEqualOrAbove = {
                    // Return the result from this lambda
                    bundle.getParcelable(EXTRA_DATA_ITEM, ShoppingItem::class.java)
                },
                onSdkBelow = {
                    // Return the result from this lambda
                    bundle.getParcelable(EXTRA_DATA_ITEM)
                }
            )
            addNewItem(shoppingItem)
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
            calculate()
        }
    }

    private fun addNewItem(shoppingItem: ShoppingItem? = null) {
        formViewModel.addNewItem(shoppingItem)
        formViewModel.newItem.observeOnce(this) { resource ->
            shoppingItemAdapter.addItem(resource)
            changeVisibility(false)
        }
    }

    private fun updateQuantity() {
        formViewModel.item.observe(this) {
            shoppingItemAdapter.updateItem(it)
        }
    }

    private fun deleteItemConfirmation(model: ShoppingItem) {
        CaldisDialog(this)
            .setTitle(getString(R.string.delete_item))
            .setSubtitle(getString(R.string.confirmation_delete))
            .setBtnNegative(getString(R.string.yes), true) {
                shoppingItemAdapter.removeItem(model)
                checkIfListIsEmpty()
            }
            .setBtnPositive(getString(R.string.no), true)
            .setOutsideTouchable(false)
            .show()
    }

    private fun checkIfListIsEmpty() {
        if (shoppingItemAdapter.itemCount > 0) return
        changeVisibility(true)
    }

    private fun showRecycleList() {
        shoppingItemAdapter.onBtnMinusClick = scope@{ model, _ ->
            if (model.itemQuantity == Config.DEFAULT_DOUBLE_VALUE_ONE) {
                deleteItemConfirmation(model)
                return@scope
            }

            formViewModel.decreaseItemQuantity(model)
            updateQuantity()
        }
        shoppingItemAdapter.onBtnPlusClick = { model, _ ->
            formViewModel.increaseItemQuantity(model)
            updateQuantity()
        }
        binding.rvItemShopping.apply {
            layoutManager = LinearLayoutManager(thisActivityContext)
            adapter = shoppingItemAdapter
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


    private fun calculate() {
        if (shoppingDetail?.discountDetail == null) {
            showShortToast("Please choose discount type")
            return
        }
        calculateDiscount()
    }

    private fun calculateDiscount() {
        formViewModel.getResultDiscount(
            shoppingDetail?.apply {
                listItem = shoppingItemAdapter.getList()
            }
        )
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
                        val result = resource.data
                        openFragment(result)
                        binding.viewLoading.root.visibility = View.GONE
                    }

                    else -> {}
                }
            }
        }
    }

    private fun openFragment(shoppingDetail: ShoppingDetail?) {
        if (shoppingDetail == null) return

        val fragment = DiscountSummaryFragment.newInstance(shoppingDetail)

        binding.flFragmentContainer.visibility = View.VISIBLE
        supportFragmentManager
            .beginTransaction()
            .replace(binding.flFragmentContainer.id, fragment, fragment.tag)
            .addToBackStack(fragment.tag)
            .commit()
    }

    @Deprecated(
        "This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.",
        ReplaceWith(
            "super.onBackPressedDispatcher.onBackPressed()",
            "androidx.appcompat.app.AppCompatActivity"
        )
    )
    override fun onBackPressed() {
        if (binding.flFragmentContainer.isVisible) {
            supportFragmentManager.popBackStack()
            binding.flFragmentContainer.visibility = View.GONE
            return
        }
        super.onBackPressedDispatcher.onBackPressed()
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_DATA_ITEM = "extra_data_item"
    }
}