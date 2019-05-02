package com.example.personapi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.personapi.api_service.FirebaseApiService
import kotlinx.android.synthetic.main.activity_add_ingredient.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NumberFormatException

class AddIngredientActivity : AppCompatActivity() {

    private val firebaseApi by lazy {
        FirebaseApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredient)

        add_ingredient_btn.setOnClickListener {
            val name = name_et.text.toString()
            val unit = unit_et.text.toString()
            val calories = calories_et.text.toString()
            if(name != "" && unit != "" && calories != ""){
                if(unit == "g" || unit == "ml"){
                    try{
                        calories.toInt()
                        val map: HashMap<String, String> = hashMapOf("calories" to calories, "name" to name, "unit" to unit)
                        firebaseApi.insertIngredient(map).enqueue(object: Callback<String>{
                            override fun onFailure(call: Call<String>, t: Throwable) {
                                println(t.message)
                            }
                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                println(response.message())
                            }
                        })
                        name_et.setText("")
                        unit_et.setText("")
                        calories_et.setText("")
                        Toast.makeText(applicationContext, "Successfully stored the ingredient object to the database", Toast.LENGTH_LONG).show()
                    } catch (nfe: NumberFormatException){
                        Toast.makeText(this, "Value for calories must be a integer number.", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this, "Unit must be either 'g' or 'ml'.", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this, "None of the fields can be empty.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
