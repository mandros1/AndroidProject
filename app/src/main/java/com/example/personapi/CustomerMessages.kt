package com.example.personapi

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.personapi.models.Customer
import com.example.personapi.view_models.CustomerName
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_customer_messages.*

class CustomerMessages : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_messages)

        val adapter = GroupAdapter<ViewHolder>()
        customerMessageID.adapter = adapter

        getAllCustomer()
    }

    companion object {
        val USER_KEY = "USER_KEY"
    }

    private fun getAllCustomer(){
        val ref = FirebaseDatabase.getInstance().getReference("/customers")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    val user = it.getValue(Customer::class.java)
                    if(user != null){
                        adapter.add(CustomerName(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val customer = item as CustomerName
                    val intent = Intent(view.context, ChatView::class.java)
                    intent.putExtra(USER_KEY, customer.customer)
                    startActivity(intent)
                }
                customerMessageID.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {}
        })
    }
}
