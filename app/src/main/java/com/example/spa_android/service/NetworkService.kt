package com.example.spa_android.service

import com.example.spa_android.retrofit.RegisterUserModel
import com.example.spa_android.retrofit.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NetworkService {
    @GET("/fireEntity/list")
    fun doGetUserList(): Call<ArrayList<UserModel>>
    @POST("/fireEntity/save")
    fun saveUser(@Body user: RegisterUserModel): Call<RegisterUserModel>

}