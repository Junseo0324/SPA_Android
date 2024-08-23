package com.example.spa_android.retrofit

import android.app.Application
import com.example.spa_android.service.NetworkService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApplication : Application() {
    companion object{
        private val BASE_URL = "http://localhost:8080"

        val networkService: NetworkService
        val gson = GsonBuilder().setLenient().create()
        private val retrofit: Retrofit
            get() = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        init {
            networkService = retrofit.create(NetworkService::class.java)
        }
    }
}