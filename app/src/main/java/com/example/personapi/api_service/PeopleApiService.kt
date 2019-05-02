package com.example.personapi.api_service

import com.example.personapi.models.Request
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

interface PeopleApiService {

    @GET(".")
    fun getPeople(@Query("page") page: String,
                      @Query("results") results: String,
                      @Query("used") used: String,
                      @Query("inc") inc: String): Observable<Request>

    companion object {
        fun create(): PeopleApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://randomuser.me/api/")
                .build()

            return retrofit.create(PeopleApiService::class.java)
        }
    }
}