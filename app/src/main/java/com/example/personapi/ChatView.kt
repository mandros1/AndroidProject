package com.example.personapi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.personapi.models.ChatMessage
import com.example.personapi.models.Customer
import com.example.personapi.view_models.ChatMessageFrom
import com.example.personapi.view_models.ChatMessageTo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_view.*

class ChatView : AppCompatActivity() {

    var adapter = GroupAdapter<ViewHolder>()
    var toCoustomer: Customer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_view)
        sendMessageRecID.adapter = adapter
        toCoustomer = intent.getParcelableExtra(CustomerMessages.USER_KEY)
        listenForMessage()

        sendMessageButton.setOnClickListener(){
            sendMessage()
        }
    }

    private fun sendMessage(){
        val text = sendMessageTextField.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val customer = intent.getParcelableExtra<Customer>(CustomerMessages.USER_KEY)
        val toId = customer.uid
        val reference = FirebaseDatabase.getInstance().getReference("/messages-all/$fromId/$toId").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/messages-all/$toId/$fromId").push()

        val chatMessage = ChatMessage(reference.key, text, fromId, toId, System.currentTimeMillis()/1000)
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                sendMessageTextField.setText("")
            }
            .addOnFailureListener(){

            }
        toReference.setValue(chatMessage)
    }

    private fun listenForMessage(){
        val fromid = FirebaseAuth.getInstance().uid
        val toId = toCoustomer?.uid
        val reference = FirebaseDatabase.getInstance().getReference("/messages-all/$fromid/$toId")

        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                    if(chatMessage != null){
                        if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
                            adapter.add(ChatMessageTo(chatMessage.text))
                        }else {
                            adapter.add(ChatMessageFrom(chatMessage.text))
                        }

                    }
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
}
