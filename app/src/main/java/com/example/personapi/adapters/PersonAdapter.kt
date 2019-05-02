package com.example.personapi.adapters

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.personapi.R
import com.example.personapi.databinding.PersonItemViewBinding
import com.example.personapi.models.PersonItemViewModel

typealias TapAction = (PersonItemViewModel, Int) -> Unit

class PersonAdapter(
    private val tapAction: TapAction,
    private var personList: MutableList<PersonItemViewModel>
    ): RecyclerView.Adapter<PersonAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun getItemCount(): Int = personList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(personList[position], tapAction)
    }


    class ViewHolder(val parent: ViewGroup,
                     val binding: PersonItemViewBinding = DataBindingUtil.inflate(
                         LayoutInflater.from(parent.context),
                         R.layout.person_item,
                         parent,
                         false)): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PersonItemViewModel, tapAction: TapAction) {
            // Use item to set image and text for labels

            binding.root.setOnClickListener {
                tapAction(item, layoutPosition)
            }

            binding.viewModel = item
        }
    }
}