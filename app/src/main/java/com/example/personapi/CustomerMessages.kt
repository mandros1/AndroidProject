package com.example.personapi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.personapi.view_models.CustomerName
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_customer_messages.*

class CustomerMessages : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_messages)

        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(CustomerName())
        adapter.add(CustomerName())
        adapter.add(CustomerName())

        customerMessageID.adapter = adapter
    }
}
