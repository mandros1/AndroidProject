package com.example.personapi.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.personapi.R
import com.example.personapi.databinding.FridgeItemViewBinding
import com.example.personapi.models.FridgeItemViewModel

typealias FridgeTapAction = (FridgeItemViewModel, Int) -> Unit

class FridgeAdapter(
    private val tapAction: FridgeTapAction,
    private var fridge_items_list: MutableList<FridgeItemViewModel>
): RecyclerView.Adapter<FridgeAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun getItemCount(): Int = fridge_items_list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(fridge_items_list[position], tapAction)
    }


    class ViewHolder(val parent: ViewGroup,
                     val binding: FridgeItemViewBinding = DataBindingUtil.inflate(
                         LayoutInflater.from(parent.context),
                         R.layout.fridge_item,
                         parent,
                         false)): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FridgeItemViewModel, tapAction: FridgeTapAction) {
            // Use item to set image and text for labels

            binding.root.setOnClickListener {
                tapAction(item, layoutPosition)
            }

            binding.viewModel = item
        }
    }
}