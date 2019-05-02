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
import com.example.personapi.databinding.FridgeDetailFragmentBinding
import com.example.personapi.view_models.FridgeDetailViewModel

class FridgeDetail: Fragment() {

    companion object {
        fun newInstance() = FridgeDetail()
    }

    private lateinit var viewModel: FridgeDetailViewModel

    private lateinit var binding: FridgeDetailFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fridge_detail_fragment,
            container,
            false)

        // Return the actual View
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(FridgeDetailViewModel::class.java)
        }

        // Observe changes in the ViewModel
        viewModel.fridge_item.observe(this, Observer {
                fridge_item ->

            // Check if recipe exists
            fridge_item?.let {
                // Set the binding ViewModel
                binding.viewModel = it
            }
        })

    }
}