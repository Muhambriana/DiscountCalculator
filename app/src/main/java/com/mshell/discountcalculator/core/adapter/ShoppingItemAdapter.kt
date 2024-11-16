package com.mshell.discountcalculator.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mshell.discountcalculator.core.models.ShoppingItem
import com.mshell.discountcalculator.databinding.ItemListShoppingBinding
import com.mshell.discountcalculator.utils.helper.Helper.format
import com.mshell.discountcalculator.utils.helper.Helper.toCurrency

class ShoppingItemAdapter(private val onlyPreview: Boolean = true) :
    RecyclerView.Adapter<ViewHolder>() {

        private var listShoppingItem = mutableListOf<ShoppingItem>()
        var onItemClick: ((ShoppingItem) -> Unit)? = null
        var onBtnMinusClick: ((ShoppingItem, Int) -> Unit)? = null
        var onBtnPlusClick: ((ShoppingItem, Int) -> Unit)? = null

        fun setItemList(items: List<ShoppingItem>?) {
            if (items == null) return
            listShoppingItem.clear()
            listShoppingItem.addAll(items)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return when(onlyPreview) {
                true -> {
                    val binding = ItemListShoppingBinding.inflate(inflater, parent, false)
                    OnlyPreviewShoppingItemViewHolder(binding)
                }
                false -> {
                    val binding = ItemListShoppingBinding.inflate(inflater, parent, false)
                    ShoppingItemViewHolder(binding)
                }
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val chat = listShoppingItem[position]
            if (onlyPreview) {
                (holder as OnlyPreviewShoppingItemViewHolder).bind(chat)
            } else {
                (holder as ShoppingItemViewHolder).bind(chat)
            }
        }

        override fun getItemCount(): Int {
            return listShoppingItem.size
        }

        inner class ShoppingItemViewHolder(private val binding: ItemListShoppingBinding) :
            ViewHolder(binding.root) {
                fun bind(shoppingItem: ShoppingItem?) {
                    with(binding) {
                        shoppingItem?.totalPrice = shoppingItem?.pricePerUnit?.times(shoppingItem.quantity ?: 1.0)
                        tvItemName.text = shoppingItem?.itemName
                        edQuantity.setText(shoppingItem?.quantity?.format())
                        tvPricePerUnit.text = shoppingItem?.pricePerUnit?.toCurrency()
                        tvTotalPrice.text = shoppingItem?.totalPrice?.toCurrency()
                    }
                }

                init {
                    binding.root.setOnClickListener {
                        onItemClick?.invoke(listShoppingItem[adapterPosition])
                    }
                    binding.btnMinus.setOnClickListener {
                        try {
                            onBtnMinusClick?.invoke(listShoppingItem[adapterPosition], adapterPosition)
                        } catch (e:Exception) {
                            e.printStackTrace()
                        }
                    }
                    binding.btnPlus.setOnClickListener {
                        onBtnPlusClick?.invoke(listShoppingItem[adapterPosition], adapterPosition)
                    }
                }
        }

        inner class OnlyPreviewShoppingItemViewHolder(private val binding: ItemListShoppingBinding) :
            ViewHolder(binding.root) {
                fun bind(shoppingItem: ShoppingItem?) {
                    with(binding) {
                        btnMinus.visibility = View.GONE
                        btnPlus.visibility = View.GONE
                        tvItemName.text = shoppingItem?.itemName
                        edQuantity.setText(shoppingItem?.quantity?.format())
                        tvPricePerUnit.text = shoppingItem?.pricePerUnit?.toCurrency()
                        tvTotalPrice.text = shoppingItem?.totalPriceAfterDiscount?.toCurrency(2)
                    }
                }

            init {
                binding.root.setOnClickListener {
                    onItemClick?.invoke(listShoppingItem[adapterPosition])
                }
            }
        }

        fun getList(): MutableList<ShoppingItem> {
            return listShoppingItem
        }

        fun updateItem(item: ShoppingItem?) {
            listShoppingItem.find { it.id == item?.id }?.apply {
                itemName = item?.itemName
                pricePerUnit = item?.pricePerUnit
                quantity = item?.quantity
                totalDiscount = item?.totalDiscount
                totalPriceAfterDiscount = item?.totalPriceAfterDiscount
            }
            notifyItemChanged(listShoppingItem.indexOf(item))
        }

        fun addItem(item: ShoppingItem?) {
            if (item == null) return
            listShoppingItem.add(item)
            notifyItemInserted(listShoppingItem.size)
        }

    fun removeItem(shoppingItemId: Long) {
        listShoppingItem.indexOfFirst { it.id == shoppingItemId }
            .takeIf { it >= 0 }
            ?.let {
                listShoppingItem.removeAt(it)
                notifyItemRemoved(it)
            }
    }


}