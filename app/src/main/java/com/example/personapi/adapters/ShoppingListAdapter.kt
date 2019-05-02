package com.example.personapi.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.personapi.R
import com.example.personapi.models.ShoppingItemViewModel
import com.example.personapi.databinding.ShoppingItemViewBinding

typealias ShoppingTapAction = (ShoppingItemViewModel, Int) -> Unit

class ShoppingListAdapter(
    private val tapAction: ShoppingTapAction,
    private var shopping_items_list: MutableList<ShoppingItemViewModel>
): RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun getItemCount(): Int = shopping_items_list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shopping_items_list[position], tapAction)
    }


    class ViewHolder(val parent: ViewGroup,
                     val binding: ShoppingItemViewBinding = DataBindingUtil.inflate(
                         LayoutInflater.from(parent.context),
                         R.layout.shopping_item,
                         parent,
                         false)): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShoppingItemViewModel, tapAction: ShoppingTapAction) {
            // Use item to set image and text for labels

            binding.root.setOnClickListener {
                tapAction(item, layoutPosition)
            }

            binding.viewModel = item
        }
    }
}