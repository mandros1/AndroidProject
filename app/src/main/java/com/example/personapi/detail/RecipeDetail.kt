package com.example.personapi.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.personapi.view_models.RecipeDetailViewModel
import com.example.personapi.R
import com.example.personapi.databinding.RecipeDetailFragmentBinding

class RecipeDetail: Fragment() {
    companion object {
        fun newInstance() = RecipeDetail()
    }

    private lateinit var viewModel: RecipeDetailViewModel

    private lateinit var binding: RecipeDetailFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Create binding
        binding = DataBindingUtil.inflate(inflater,
            R.layout.recipe_detail_fragment,
            container,
            false)

        // Return the actual View
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(RecipeDetailViewModel::class.java)
        }

        // Observe changes in the ViewModel
        viewModel.recipe.observe(this, Observer {
                recipe ->

            // Check if recipe exists
            recipe?.let {
                // Set the binding ViewModel
                binding.viewModel = it
            }
        })

    }
}