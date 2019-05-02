package com.example.personapi.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.personapi.R
import com.example.personapi.databinding.RecipeItemViewBinding
import com.example.personapi.models.RecipeItemViewModel

typealias TapAction = (RecipeItemViewModel, Int) -> Unit

class RecipeAdapter(
    private val tapAction: TapAction,
    private var recipeList: MutableList<RecipeItemViewModel>
    ): RecyclerView.Adapter<RecipeAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun getItemCount(): Int = recipeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipeList[position], tapAction)
    }


    class ViewHolder(val parent: ViewGroup,
                     val binding: RecipeItemViewBinding = DataBindingUtil.inflate(
                         LayoutInflater.from(parent.context),
                         R.layout.recipe_item,
                         parent,
                         false)): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecipeItemViewModel, tapAction: TapAction) {
            // Use item to set image and text for labels

            binding.root.setOnClickListener {
                tapAction(item, layoutPosition)
            }

            binding.viewModel = item
        }
    }
}