package com.example.personapi

import com.example.personapi.models.Request
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface FirebaseApiService {

    @GET("recipe_list/recipes.json")
    fun getRecipes(): Observable<Request>

    @GET("ingredient_list/ingredients.json")
    fun getIngredients(): Observable<Request>

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