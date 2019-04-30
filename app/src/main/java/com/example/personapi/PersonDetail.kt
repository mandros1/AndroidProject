package com.example.personapi

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.personapi.databinding.PersonDetailFragmentBinding

class PersonDetail: Fragment() {
    companion object {
        fun newInstance() = PersonDetail()
    }

    private lateinit var viewModel: PersonDetailViewModel

    // Binding for xml (PersonDetailFragmentBinding only exists after we build the project)
    private lateinit var binding: PersonDetailFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Create binding
        binding = DataBindingUtil.inflate(inflater,
            R.layout.person_detail_fragment,
            container,
            false)

        // Return the actual View
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(PersonDetailViewModel::class.java)
        }

        // Observe changes in the ViewModel
        viewModel.person.observe(this, Observer {
                person ->

            // Check if person exists
            person?.let {
                // Set the binding ViewModel
                binding.viewModel = it
            }
        })

    }
}