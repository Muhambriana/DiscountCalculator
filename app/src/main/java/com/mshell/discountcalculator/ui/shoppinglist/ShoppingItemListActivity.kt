package com.mshell.discountcalculator.ui.shoppinglist

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.mshell.discountcalculator.R
import com.mshell.discountcalculator.core.adapter.ShoppingItemAdapter
import com.mshell.discountcalculator.core.data.source.CalDisResource
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.core.models.ShoppingDetail
import com.mshell.discountcalculator.databinding.ActivityShoppingItemListBinding
import com.mshell.discountcalculator.ui.discountsummary.DiscountSummaryFragment
import com.mshell.discountcalculator.ui.itemdetail.ItemDetailBottomFragment
import com.mshell.discountcalculator.utils.config.Config
import com.mshell.discountcalculator.utils.config.DataEvent
import com.mshell.discountcalculator.utils.helper.Helper
import com.mshell.discountcalculator.utils.helper.Helper.showErrorToast
import com.mshell.discountcalculator.utils.view.CaldisDialog
import com.mshell.discountcalculator.utils.view.setSingleClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class ShoppingItemListActivity : AppCompatActivity() {

    private val thisActivityContext by lazy {
        this
    }
    private val binding by lazy {
        ActivityShoppingItemListBinding.inflate(layoutInflater)
    }

    private val shoppingItemAdapter by lazy {
        ShoppingItemAdapter(false)
    }

    private val shoppingItemListViewModel: ShoppingItemListViewModel by viewModel()
    private var shoppingId: Long? = null
    private var shoppingDetail: ShoppingDetail? = null
    private var dataEvent: DataEvent? = null


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

    private fun setDataToModels() {
        shoppingId = intent?.extras?.getLong(EXTRA_SHOPPING_ID)
        getShoppingDetail()
    }

    private fun getShoppingDetail() {
        shoppingId?.let { id ->
            shoppingItemListViewModel.getShoppingDetail(id).observe(this) { resource ->
                when (resource) {
                    is CalDisResource.Empty -> {}
                    is CalDisResource.Error -> {
                        binding.viewLoading.root.visibility = View.GONE
                        showErrorToast(resource.exceptionTypeEnum?.codeAsString.toString())
                    }
                    is CalDisResource.Loading -> {
                        binding.viewLoading.root.visibility = View.VISIBLE
                    }
                    is CalDisResource.Success -> {
                        resource.data.also {
                            if (it == null) {
                                showErrorToast("Failed")
                                return@observe
                            }

                            handlingData(it)
                            binding.viewLoading.root.visibility = View.GONE
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun handlingData(shoppingDetail: ShoppingDetail?) {

        this.shoppingDetail = shoppingDetail

        shoppingDetail?.listItem?.also { list ->
            if (binding.clgNotEmpty.isVisible.not() && list.isEmpty().not()) changeVisibility(false)

            when(dataEvent) {
                DataEvent.INSERT -> {
                    eventInserted(list.last())
                }
                DataEvent.UPDATE -> {
                    val item = list.find { it.id == DataEvent.UPDATE.id }
                    if (item == null) return

                    eventUpdated(item)
                }
                DataEvent.DELETE -> {
                    val shoppingItemId = DataEvent.DELETE.id
                    eventDeleted(shoppingItemId)
                }
                null -> {}
            }

        }
    }

    private fun eventInserted(shoppingItem: ShoppingItem) {
        shoppingItemAdapter.addItem(shoppingItem)
    }

    private fun eventUpdated(shoppingItem: ShoppingItem) {
        shoppingItemAdapter.updateItem(shoppingItem)
    }

    private fun eventDeleted(shoppingItemId: Long) {
        shoppingItemAdapter.removeItem(shoppingItemId)
        checkIfListIsEmpty()
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
        supportFragmentManager.setFragmentResultListener(
            ItemDetailBottomFragment.KEY_UPDATE_ITEM,
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
            updateItem(shoppingItem)
        }
        showRecycleList()
    }

    private fun initButton() {
        binding.btnDeleteItem.setSingleClickListener {

        }
        binding.btnAddItem.setSingleClickListener {
            openItemDetailFragment()
        }
//        binding.btnCalculate.setOnClickListener {
//            calculate()
//        }
    }

    private fun openItemDetailFragment(shoppingItem: ShoppingItem? = null) {
        val bottomDialogFragment = ItemDetailBottomFragment.newInstance(shoppingItem)
        bottomDialogFragment.show(supportFragmentManager, "")
    }

    private fun addNewItem(shoppingItem: ShoppingItem? = null) {
        shoppingId?.also {
            if (shoppingItem == null) return
            dataEvent = DataEvent.INSERT
            shoppingItem.shoppingId = it
            shoppingItemListViewModel.insertShoppingItem(shoppingItem)
        }
    }

    private fun deleteItemConfirmation(model: ShoppingItem) {
        CaldisDialog(this)
            .setTitle(getString(R.string.delete_item))
            .setSubtitle(getString(R.string.confirmation_delete))
            .setBtnNegative(getString(R.string.yes), true) {
                deleteItem(model)
            }
            .setBtnPositive(getString(R.string.no), true)
            .setOutsideTouchable(false)
            .show()
    }

    private fun deleteItem(model: ShoppingItem) {
        dataEvent = DataEvent.DELETE.withId(model.id)
        shoppingItemListViewModel.deleteShoppingItem(model)
    }

    private fun checkIfListIsEmpty() {
        if (shoppingItemAdapter.itemCount > 0) return
        changeVisibility(true)
    }

    private fun showRecycleList() {
        shoppingItemAdapter.onBtnMinusClick = scope@{ model, _ ->
            if (model.quantity == Config.DEFAULT_DOUBLE_VALUE_ONE) {
                deleteItemConfirmation(model)
                return@scope
            }

            model.quantity = model.quantity.minus(1)
            updateItem(model)
        }
        shoppingItemAdapter.onBtnPlusClick = { model, _ ->
            model.quantity = model.quantity.plus(1)
            updateItem(model)
        }
        shoppingItemAdapter.onItemClick = { model ->
            openItemDetailFragment(model)
        }
        binding.rvItemShopping.apply {
            layoutManager = LinearLayoutManager(thisActivityContext)
            adapter = shoppingItemAdapter
        }
    }

    private fun updateItem(model: ShoppingItem?) {
        if (model == null) return

        dataEvent = DataEvent.UPDATE.withId(model.id)
        shoppingItemListViewModel.updateShoppingItem(model)
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


//    private fun calculate() {
//        if (shoppingDetail?.discountDetail == null) {
//            showShortToast("Please choose discount type")
//            return
//        }
//        calculateDiscount()
//    }
//
//    private fun calculateDiscount() {
//        formViewModel.getResultDiscount(
//            shoppingDetail?.apply {
//                listItem = shoppingItemAdapter.getList()
//            }
//        )
//        observeResult()
//    }
//
//    private fun observeResult() {
//        formViewModel.discountResult.observe(this) { event ->
//            event.getContentIfNotHandled().let { resource ->
//                when (resource) {
//                    is CalDisResource.Loading -> {
//                        binding.viewLoading.root.visibility = View.VISIBLE
//                    }
//
//                    is CalDisResource.Empty -> {
//                        binding.viewLoading.root.visibility = View.GONE
//                        binding.viewEmpty.root.visibility = View.VISIBLE
//                        binding.btnCalculate.visibility = View.GONE
//                        binding.btnDeleteItem.visibility = View.GONE
//                    }
//
//                    is CalDisResource.Error -> {
//                        showShortToast(resource.error?.message.toString())
//                        binding.viewLoading.root.visibility = View.GONE
//                    }
//
//                    is CalDisResource.Success -> {
//                        val result = resource.data
//                        openFragment(result)
//                        binding.viewLoading.root.visibility = View.GONE
//                    }
//
//                    else -> {}
//                }
//            }
//        }
//    }

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

    @SuppressLint("MissingSuperCall")
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
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_SHOPPING_ID = "extra_shopping_id"
        const val EXTRA_DATA_ITEM = "extra_data_item"
    }
}