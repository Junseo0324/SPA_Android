package com.example.spa_android.service

import com.example.spa_android.data.ChatingItem
import com.example.spa_android.retrofit.BoardModel
import com.example.spa_android.retrofit.ChatModel
import com.example.spa_android.retrofit.ChatRequestModel
import com.example.spa_android.retrofit.ChatSummaryDTO
import com.example.spa_android.retrofit.UserModel
import com.example.spa_android.retrofit.UserRequestModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkService {
    @GET("/fireEntity/list") //회원 정보
    fun doGetUserList(): Call<ArrayList<UserModel>>

    @POST("/fireEntity/save") //회원가입
    fun saveUser(@Body user: UserRequestModel): Call<UserRequestModel>

    @Multipart
    @PUT("/fireEntity/update/{email}") //회원 정보 수정
    fun updateUser(@Path("email") email: String, @Part("fireDTO") user: RequestBody,@Part filePath: MultipartBody.Part)
    : Call<Map<String,String>>

    @DELETE("/fireEntity/delete/{id}") //회원 탈퇴
    fun deleteUser(@Path("id") itemId: Long): Call<Void>

    @GET("/api/board/list") //게시판 게시글
    fun getBoardList(): Call<ArrayList<BoardModel>>

    @Multipart
    @POST("/api/board/write") //게시판 글쓰기
    fun writeBoard(@Part("boardDTO") board: RequestBody,@Part filePath: MultipartBody.Part) : Call<Map<String,String>>

    @GET("/api/chat/list/{receiver}")
    fun getChatList(@Path("receiver") receiver: String): Call<ArrayList<ChatModel>>

    @GET("/api/chat/chatting/{chatName}")
    fun getChattingList(@Path("chatName") chatName: String): Call<ArrayList<ChatModel>>


    @GET("/api/chat/chattingItems")
    fun getChattingItems(@Query("chatName") chatName: String, @Query("sharedEmail") sharedEmail: String): Call<List<ChatingItem>>
    @POST("/api/chat/send")
    fun sendMessage(@Body chatDTO: ChatRequestModel): Call<ChatRequestModel>
    @GET("api/chat/summary/{userEmail}")
    fun getChatListByUser(@Path("userEmail") userEmail: String): Call<ArrayList<ChatSummaryDTO>>

    @PUT("api/chat/read/{sender}/{receiver}")
    fun markMessageAsRead(@Path("sender") sender: String, @Path("receiver") receiver: String): Call<Void>

}