package com.mshell.discountcalculator.core.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mshell.discountcalculator.R
import com.mshell.discountcalculator.core.models.Form
import com.mshell.discountcalculator.databinding.ItemListFormBinding

class FormAdapter: RecyclerView.Adapter<FormAdapter.FormViewHolder>() {

    private var listForm = mutableListOf<Form>()
    var onItemClick: ((Form) -> Unit)? = null

    fun setItemList(attendances: List<Form>?) {
        if (attendances == null) return
        listForm.clear()
        listForm.addAll(attendances)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FormViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_form, parent,false))

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
            binding.edItemName.setText(form.itemName)
            binding.edItemPrice.setText(form.itemPrice.toString())
            binding.edItemQuantity.setText(form.itemQuantity.toString())
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listForm[adapterPosition])
            }
        }
    }

}