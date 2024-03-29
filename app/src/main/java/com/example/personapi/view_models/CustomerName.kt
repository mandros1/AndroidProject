package com.example.personapi.view_models

import com.example.personapi.R
import com.example.personapi.models.Customer
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.new_customer.view.*

class CustomerName(val customer: Customer) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.customerNameID.text = customer.userName
    }

    override fun getLayout(): Int {
        return R.layout.new_customer
    }
}