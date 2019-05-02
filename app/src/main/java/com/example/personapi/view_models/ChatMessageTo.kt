package com.example.personapi.view_models

import com.example.personapi.R
import com.example.personapi.models.Customer
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.message_row.view.*
import kotlinx.android.synthetic.main.message_row_new.view.*

class ChatMessageTo(val text: String): Item <ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chatMessageTo.text = text
    }

    override fun getLayout(): Int {
        return R.layout.message_row_new
    }
}