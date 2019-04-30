package com.example.personapi

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var emailField = ""
    var passwordField = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        registerTextView.setOnClickListener(){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener(){
            checkFields()
        }
    }

    fun checkFields(){

        emailField = emailLoginTextField.text.toString()
        passwordField = passwordLoginTextField.text.toString()

        if(emailField.isNullOrBlank() || passwordField.isNullOrBlank()){
            createToast("Please enter email and password to login")
        } else {
            loginToFirebase(emailField, passwordField)
        }
    }

    fun loginToFirebase(email: String, password: String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(){
                if(!it.isSuccessful) return@addOnCompleteListener
                createToast("Login was successful")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            .addOnFailureListener(){
                createToast("Login Failed, Email or Password wrong")
            }
    }

    fun createToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}