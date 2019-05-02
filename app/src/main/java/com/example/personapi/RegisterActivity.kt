package com.example.personapi

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.personapi.models.Customer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    var emailField = ""
    var passwordField = ""
    var userNameField = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        loginTextView.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        registerButton.setOnClickListener(){
            checkFields()
        }
    }

    fun checkFields(){
        emailField = registerInputEmail.text.toString()
        passwordField = registerInputPassword.text.toString()
        userNameField = usernameField.text.toString()

        if(emailField.isNullOrBlank() || passwordField.isNullOrBlank() || userNameField.isNullOrBlank()){
            createToast("Please fill in all required fields")
        } else{
            if (passwordField.length < 5){
                createToast("Password needs to be longer than 5 characters")
            } else {
                registerToFirebase(emailField, passwordField)
            }
        }
    }

    fun createToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun registerToFirebase(email: String, password: String){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(){
                if(!it.isSuccessful) return@addOnCompleteListener
                savingToDatabase()
                createToast("You have been successfully registered")
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener(){
                createToast("Registration failure")
            }
    }

    fun savingToDatabase(){
        val uid = FirebaseAuth.getInstance().uid
        val reference = FirebaseDatabase.getInstance().getReference("customers/$uid")
        val customer = Customer(uid, usernameField.text.toString())
        reference.setValue(customer)
            .addOnSuccessListener {
                Log.e("RegisterActivity", "Success")
            }

            .addOnFailureListener(){
                Log.e("RegisterActivity", "Failure")
            }

    }

}