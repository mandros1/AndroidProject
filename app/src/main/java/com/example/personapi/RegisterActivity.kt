package com.example.personapi

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    var emailField = ""
    var passwordField = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        loginTextView.setOnClickListener(){
            finish()
        }

        registerButton.setOnClickListener(){
            checkFields()
        }
    }

    fun checkFields(){
        emailField = registerInputEmail.text.toString()
        passwordField = registerInputPassword.text.toString()

        if(emailField.isNullOrBlank() || passwordField.isNullOrBlank()){
            createToast("Please enter email and password to register")
        } else{
            registerToFirebase(emailField, passwordField)
        }
    }

    fun createToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun registerToFirebase(email: String, password: String){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(){
                if(!it.isSuccessful) return@addOnCompleteListener
                createToast("You have been successfully registered")
                finish()
            }
            .addOnFailureListener(){
                createToast("Registration failure")
            }
    }
}