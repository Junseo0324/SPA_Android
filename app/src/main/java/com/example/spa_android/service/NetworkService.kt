package com.example.spa_android.service

import retrofit2.http.GET

interface NetworkService {
    @GET("/user/list")
    fun doGetUserList()

}