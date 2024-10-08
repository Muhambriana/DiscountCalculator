package com.mshell.discountcalculator.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.mshell.discountcalculator.R
import com.mshell.discountcalculator.core.models.Form
import com.mshell.discountcalculator.databinding.ItemListFormBinding
import com.mshell.discountcalculator.utils.view.setSingleClickListener

class FormAdapter : RecyclerView.Adapter<FormAdapter.FormViewHolder>() {

    private var listForm = mutableListOf<Form>()
    var onItemClick: ((Form) -> Unit)? = null

    fun addItem(item: Form?) {
        if (item == null) return
        listForm.add(item)
        notifyItemInserted(listForm.size)
    }

    fun setItemList(items: List<Form>?) {
        if (items == null) return
        listForm.clear()
        listForm.addAll(items)
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
        fun bind(form: Form) {
            val originalPrice = form.itemPrice
            binding.tvItemName.text = form.itemName
            binding.tvItemOriginalPrice.text = if (originalPrice == null) "" else "Rp. $originalPrice"
            binding.edItemQuantity.setText(form.itemQuantity?.toString() ?: "")


            binding.edItemQuantity.doAfterTextChanged { value ->
                form.itemQuantity = value.toString().let {
                    if (it == "") 0.0
                    else it.toDouble()
                }
            }

            binding.btnPlus.setSingleClickListener {
                form.itemQuantity = (form.itemQuantity?.plus(1.0)) ?: 1.0
                notifyItemChanged(adapterPosition)
            }
            binding.btnMinus.setSingleClickListener {
                form.itemQuantity = (form.itemQuantity?.minus(1.0))
                notifyItemChanged(adapterPosition)

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

}