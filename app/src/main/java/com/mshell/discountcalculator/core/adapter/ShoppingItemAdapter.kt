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
            notifyItemRangeInserted(0, items.size)
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
                        shoppingItem?.totalPrice = shoppingItem?.itemPrice?.times(shoppingItem.itemQuantity ?: 1.0)
                        tvItemName.text = shoppingItem?.itemName
                        edItemQuantity.setText(shoppingItem?.itemQuantity?.format())
                        tvItemOriginalPrice.text = shoppingItem?.itemPrice?.toCurrency()
                        tvItemDiscountedPrice.text = shoppingItem?.totalPrice?.toCurrency()
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
                        edItemQuantity.setText(shoppingItem?.itemQuantity?.format())
                        tvItemOriginalPrice.text = shoppingItem?.itemPrice?.toCurrency()
                        tvItemDiscountedPrice.text = shoppingItem?.totalAfterDiscount?.toCurrency(2)
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
            listShoppingItem.find { it.itemId == item?.itemId }?.apply {
                itemName = item?.itemName
                itemPrice = item?.itemPrice
                itemQuantity = item?.itemQuantity
                totalDiscount = item?.totalDiscount
                totalAfterDiscount = item?.totalAfterDiscount
            }
            notifyItemChanged(listShoppingItem.indexOf(item))
        }

        fun addItem(item: ShoppingItem?) {
            if (item == null) return
            listShoppingItem.add(item)
            notifyItemInserted(listShoppingItem.size)
        }

        fun removeItem(shoppingItem: ShoppingItem?) {
            val index = listShoppingItem.indexOf(shoppingItem)
            if (shoppingItem == null) return
            listShoppingItem.remove(shoppingItem)
            notifyItemRemoved(index)
        }

}