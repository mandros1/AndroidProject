package com.example.personapi.api_service

import com.example.personapi.models.*
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.POST
import okhttp3.ResponseBody
import retrofit2.http.DELETE





interface FirebaseApiService {

    @GET("recipe_list/recipes.json")
    fun getRecipes(): Observable<Recipes>


    // Ingredients handling
    @GET("ingredient_list/ingredients.json")
    fun getIngredients(): Observable<Ingredients>

    @POST("ingredient_list/ingredients.json")
    @Headers(
        "Accept-Encoding: gzip,deflate",
        "Content-Type: application/x-www-form-urlencoded",
        "Accept: Application/Json",
        "User-Agent: Retrofit 2.3.0"
    )
    fun insertIngredient(
        @Body body: HashMap<String, String>
    ): Call<String>


    // Shopping cart handling
    @GET("shopping_cart/shopping_items.json")
    fun getShoppingCart(): Observable<ShoppingList>

    @POST("shopping_cart/shopping_items.json")
    @Headers(
        "Accept-Encoding: gzip,deflate",
        "Content-Type: application/x-www-form-urlencoded",
        "Accept: Application/Json",
        "User-Agent: Retrofit 2.3.0"
    )
    fun insertShoppingItem(
        @Body body: HashMap<String, String>
    ): Call<String>


    @DELETE("shopping_cart/shopping_items.json")
    fun deleteAllShoppingItems(): Call<String?>


    // Fridge items handling
    @GET("fridge/fridge_ingredients.json")
    fun getFridgeItems(): Observable<FridgeItems>

    @POST("fridge/fridge_ingredients.json")
    @Headers(
        "Accept-Encoding: gzip,deflate",
        "Content-Type: application/x-www-form-urlencoded",
        "Accept: Application/Json",
        "User-Agent: Retrofit 2.3.0"
    )
    fun insertFridgeItem(
        @Body body: HashMap<String, String>
    ): Call<String>

    @DELETE("fridge/fridge_ingredients.json")
    fun deleteAllFridgeItems(): Call<String?>


    @PUT("fridge/fridge_ingredients.json")
    @Headers(
        "Accept-Encoding: gzip,deflate",
        "Content-Type: application/x-www-form-urlencoded",
        "Accept: Application/Json",
        "User-Agent: Retrofit 2.3.0"
    )
    fun initialInsertFridgeItem(
        @Body body: HashMap<String, HashMap<String, String>>
    ): Call<String>


    companion object {
        fun create(): FirebaseApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://mycookbook-96846.firebaseio.com/")
                .build()

            return retrofit.create(FirebaseApiService::class.java)
        }
    }
}