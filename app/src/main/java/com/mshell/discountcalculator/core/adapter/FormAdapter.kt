package com.mshell.discountcalculator.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mshell.discountcalculator.R
import com.mshell.discountcalculator.core.models.Form
import com.mshell.discountcalculator.databinding.ItemListFormBinding
import com.mshell.discountcalculator.utils.config.Config.DEFAULT_DOUBLE_VALUE_ONE
import com.mshell.discountcalculator.utils.helper.Helper.format
import com.mshell.discountcalculator.utils.helper.Helper.toCurrency
import com.mshell.discountcalculator.utils.view.setSingleClickListener

class FormAdapter : RecyclerView.Adapter<FormAdapter.FormViewHolder>() {

    private var listForm = mutableListOf<Form>()
    var onItemClick: ((Form) -> Unit)? = null
    var onBtnMinusClick: ((Int) -> Unit)? = null
    var onBtnPlusClick: ((Int) -> Unit)? = null

    fun addItem(item: Form?) {
        if (item == null) return
        listForm.add(item)
        notifyItemInserted(listForm.size)
    }

    fun setItemList(items: List<Form>?) {
        if (items == null) return
        listForm.clear()
        listForm.addAll(items)
        notifyAllItem()
    }

    fun notifyAllItem(start: Int = 0, endRange: Int = listForm.size) {
        notifyItemRangeChanged(start, endRange)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FormViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_form, parent, false)
        )

    override fun onBindViewHolder(holder: FormAdapter.FormViewHolder, position: Int) {
        val attendance = listForm[position]
        holder.bind(attendance)
    }

    override fun getItemCount(): Int {
        return listForm.size
    }

    inner class FormViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListFormBinding.bind(itemView)
        fun bind(form: Form?) {
            binding.tvItemName.text = form?.itemName
            binding.edItemQuantity.setText(form?.itemQuantity?.format())
            binding.tvItemOriginalPrice.text = form?.itemPrice?.toCurrency()
            binding.tvItemDiscountedPrice.text = form?.afterDiscount?.toCurrency(2)

            binding.btnPlus.setSingleClickListener {
                form?.itemQuantity = (form?.itemQuantity?.plus(DEFAULT_DOUBLE_VALUE_ONE)) ?: DEFAULT_DOUBLE_VALUE_ONE
                notifyItemChanged(adapterPosition)
            }

            binding.btnMinus.setSingleClickListener {
                if ((form?.itemQuantity ?: DEFAULT_DOUBLE_VALUE_ONE) <= DEFAULT_DOUBLE_VALUE_ONE) {
                    listForm.remove(form)
                    notifyItemRemoved(adapterPosition)
                    onBtnMinusClick?.invoke(listForm.size)
                    return@setSingleClickListener
                }
                form?.itemQuantity = (form?.itemQuantity?.minus(DEFAULT_DOUBLE_VALUE_ONE))
                notifyItemChanged(adapterPosition)
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listForm[adapterPosition])
            }
            binding.btnPlus.setOnClickListener {
                onBtnPlusClick?.invoke(listForm.size)
            }
        }
    }

    fun getList(): MutableList<Form> {
        return listForm
    }

}