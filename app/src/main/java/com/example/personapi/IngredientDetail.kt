package com.example.personapi

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.personapi.databinding.IngredientDetailFragmentBinding

class IngredientDetail: Fragment() {

    companion object {
        fun newInstance() = IngredientDetail()
    }

    private lateinit var viewModel: IngredientDetailViewModel

    private lateinit var binding: IngredientDetailFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.ingredient_detail_fragment,
            container,
            false)

        // Return the actual View
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(IngredientDetailViewModel::class.java)
        }

        // Observe changes in the ViewModel
        viewModel.ingredient.observe(this, Observer {
                ingredient ->

            // Check if person exists
            ingredient?.let {
                // Set the binding ViewModel
                binding.viewModel = it
            }
        })

    }

}