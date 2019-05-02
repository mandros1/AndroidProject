package com.example.personapi

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_recipe_detail.*
import android.text.method.ScrollingMovementMethod
import com.example.personapi.api_service.FirebaseApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RecipeDetail : AppCompatActivity() {


    lateinit var sharedPreferences: SharedPreferences
    lateinit var ingredientNames: List<String>
    lateinit var ingredientAmounts: List<String>
    lateinit var ingredientUnits: List<String>

    private val firebaseApi by lazy {
        FirebaseApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        sharedPreferences = getSharedPreferences("appName", Context.MODE_PRIVATE)
        loadPreferences()
        instructions_tv.movementMethod = ScrollingMovementMethod()

        to_shopping_btn.setOnClickListener {

            for ((index, name) in ingredientNames.withIndex()) {
                val map: HashMap<String, String> = hashMapOf("amount" to ingredientAmounts.get(index),
                    "name" to name,
                    "unit" to ingredientUnits.get(index))
                firebaseApi.insertShoppingItem(map).enqueue(object: Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        println(t.message)
                    }
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        println(response.message())
                    }
                })
            }
            Toast.makeText(this, "Successfully added needed ingredients to your shopping list", Toast.LENGTH_LONG).show()
        }
    }

    fun refactorStringData(instructionData: String, ingredientsData: String): String{
        var instData = instructionData.split("#|#")

        var finalString = ""
        var i = 1
        for(string in instData){
            finalString += "Step $i"
            finalString += System.getProperty("line.separator")
            finalString += string
            finalString += System.getProperty("line.separator")
            i++
        }
        instData = ingredientsData.split("#|#")
        finalString += System.getProperty("line.separator")
        finalString += "Ingredients needed:"
        for(string in instData){
            finalString += " $string,"
        }
        return finalString
    }

    private fun loadPreferences(){
        val name = sharedPreferences.getString("recipeName","")
        val imgUrl = sharedPreferences.getString("imgUrl","")
        val ingredients = sharedPreferences.getString("ingredients","")
        val units = sharedPreferences.getString("units","")
        val amounts = sharedPreferences.getString("amounts","")
        val instructions = refactorStringData(sharedPreferences.getString("instructions",""), ingredients)
        val ratings = sharedPreferences.getString("ratings","")

        ingredientNames = ingredients.split("#|#")
        ingredientUnits = units.split("#|#")
        ingredientAmounts = amounts.split("#|#")

        Glide.with(this).load(imgUrl).into(imageView3)
        name_tv.setText(name)
        println(instructions)
        ratingBar.rating = ratings.toFloat()
        instructions_tv.setText(instructions)

    }

}
