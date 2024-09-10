package com.example.spa_android.service

import com.example.spa_android.retrofit.BoardModel
import com.example.spa_android.retrofit.UserModel
import com.example.spa_android.retrofit.UserRequestModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface NetworkService {
    @GET("/fireEntity/list")
    fun doGetUserList(): Call<ArrayList<UserModel>>

    @POST("/fireEntity/save")
    fun saveUser(@Body user: UserRequestModel): Call<UserRequestModel>

    @Multipart
    @PUT("/fireEntity/update/{email}")
    fun updateUser(@Path("email") email: String, @Part("fireDTO") user: RequestBody,@Part filePath: MultipartBody.Part)
    : Call<Map<String,String>>

    @GET("/api/board/list")
    fun getBoardList(): Call<ArrayList<BoardModel>>


}