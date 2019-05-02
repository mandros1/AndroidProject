package com.example.personapi.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.personapi.R
import com.example.personapi.databinding.ShoppingDetailFragmentBinding
import com.example.personapi.view_models.ShoppingDetailViewModel

class ShoppingListDetail : Fragment() {

    companion object {
        fun newInstance() = ShoppingListDetail()
    }

    private lateinit var viewModel: ShoppingDetailViewModel

    private lateinit var binding: ShoppingDetailFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.shopping_detail_fragment,
            container,
            false)

        // Return the actual View
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(ShoppingDetailViewModel::class.java)
        }

        // Observe changes in the ViewModel
        viewModel.shopping_item.observe(this, Observer {
                shopping_item ->

            // Check if recipe exists
            shopping_item?.let {
                // Set the binding ViewModel
                binding.viewModel = it
            }
        })

    }
}