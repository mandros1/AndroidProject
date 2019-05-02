package com.example.personapi.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.personapi.R
import com.example.personapi.databinding.IngredientsItemViewBinding
import com.example.personapi.models.IngredientsItemViewModel

typealias TappedAction = (IngredientsItemViewModel, Int) -> Unit

class IngredientAdapter(
    private val tapAction: TappedAction,
    private var ingredient_list: MutableList<IngredientsItemViewModel>
): RecyclerView.Adapter<IngredientAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun getItemCount(): Int = ingredient_list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ingredient_list[position], tapAction)
    }


    class ViewHolder(val parent: ViewGroup,
                     val binding: IngredientsItemViewBinding = DataBindingUtil.inflate(
                         LayoutInflater.from(parent.context),
                         R.layout.ingredient_item,
                         parent,
                         false)): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: IngredientsItemViewModel, tapAction: TappedAction) {
            // Use item to set image and text for labels

            binding.root.setOnClickListener {
                tapAction(item, layoutPosition)
            }

            binding.viewModel = item
        }
    }
}