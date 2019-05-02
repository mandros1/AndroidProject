package com.example.personapi.view_models

import com.example.personapi.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class CustomerName : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
    }

    override fun getLayout(): Int {
        return R.layout.new_customer
    }
}