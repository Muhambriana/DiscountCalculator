package com.mshell.discountcalculator.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mshell.discountcalculator.R
import com.mshell.discountcalculator.core.models.Form
import com.mshell.discountcalculator.databinding.ItemListFormBinding
import com.mshell.discountcalculator.utils.helper.Helper.format
import com.mshell.discountcalculator.utils.helper.Helper.toCurrency

class FormAdapter(private val onlyPreview: Boolean = true) :
    RecyclerView.Adapter<ViewHolder>() {

        private var listForm = mutableListOf<Form>()
        var onItemClick: ((Form) -> Unit)? = null
        var onBtnMinusClick: ((Form, Int) -> Unit)? = null
        var onBtnPlusClick: ((Form, Int) -> Unit)? = null

        fun setItemList(items: List<Form>?) {
            if (items == null) return
            listForm.clear()
            listForm.addAll(items)
            notifyItemRangeInserted(0, items.size)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return when(onlyPreview) {
                true -> {
                    val binding = ItemListFormBinding.inflate(inflater, parent, false)
                    OnlyPreviewFormViewHolder(binding)
                }
                false -> {
                    val binding = ItemListFormBinding.inflate(inflater, parent, false)
                    FormViewHolder(binding)
                }
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val chat = listForm[position]
            if (onlyPreview) {
                (holder as OnlyPreviewFormViewHolder).bind(chat)
            } else {
                (holder as FormViewHolder).bind(chat)
            }
        }

        override fun getItemCount(): Int {
            return listForm.size
        }

        inner class FormViewHolder(private val binding: ItemListFormBinding) :
            ViewHolder(binding.root) {
                fun bind(form: Form?) {
                    with(binding) {
                        tvItemName.text = form?.itemName
                        edItemQuantity.setText(form?.itemQuantity?.format())
                        tvItemOriginalPrice.text = form?.itemPrice?.toCurrency()
                        tvItemDiscountedPrice.text = form?.itemPrice?.times(form.itemQuantity ?: 1.0).toCurrency()
                    }
                }

                init {
                    binding.root.setOnClickListener {
                        onItemClick?.invoke(listForm[adapterPosition])
                    }
                    binding.btnMinus.setOnClickListener {
                        try {
                            onBtnMinusClick?.invoke(listForm[adapterPosition], adapterPosition)
                        } catch (e:Exception) {
                            e.printStackTrace()
                        }
                    }
                    binding.btnPlus.setOnClickListener {
                        onBtnPlusClick?.invoke(listForm[adapterPosition], adapterPosition)
                    }
                }
        }

        inner class OnlyPreviewFormViewHolder(private val binding: ItemListFormBinding) :
            ViewHolder(binding.root) {
                fun bind(form: Form?) {
                    with(binding) {
                        btnMinus.visibility = View.GONE
                        btnPlus.visibility = View.GONE
                        tvItemName.text = form?.itemName
                        edItemQuantity.setText(form?.itemQuantity?.format())
                        tvItemOriginalPrice.text = form?.itemPrice?.toCurrency()
                        tvItemDiscountedPrice.text = form?.afterDiscount?.toCurrency(2)
                    }
                }

            init {
                binding.root.setOnClickListener {
                    onItemClick?.invoke(listForm[adapterPosition])
                }
            }
        }

        fun getList(): MutableList<Form> {
            return listForm
        }

        fun updateItem(item: Form?) {
            listForm.find { it.itemId == item?.itemId }?.apply {
                itemName = item?.itemName
                itemPrice = item?.itemPrice
                itemQuantity = item?.itemQuantity
                itemDiscount = item?.itemDiscount
                afterDiscount = item?.afterDiscount
            }
            notifyItemChanged(listForm.indexOf(item))
        }

        fun addItem(item: Form?) {
            if (item == null) return
            listForm.add(item)
            notifyItemInserted(listForm.size)
        }

        fun removeItem(form: Form?) {
            val index = listForm.indexOf(form)
            if (form == null) return
            listForm.remove(form)
            notifyItemRemoved(index)
        }

}