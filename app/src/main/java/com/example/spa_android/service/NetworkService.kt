package com.example.spa_android.service

import com.example.spa_android.data.ChatRequestDTO
import com.example.spa_android.data.ChatRoomDTO
import com.example.spa_android.data.FCMDTO
import com.example.spa_android.data.FCMDataDTO
import com.example.spa_android.data.MessageDTO
import com.example.spa_android.data.RoomListDTO
import com.example.spa_android.data.TokenDTO
import com.example.spa_android.retrofit.ApplicantModel
import com.example.spa_android.retrofit.BoardModel
import com.example.spa_android.retrofit.ContentDTO
import com.example.spa_android.retrofit.FileDTO
import com.example.spa_android.retrofit.ProjectContentEntity
import com.example.spa_android.retrofit.ProjectListModel
import com.example.spa_android.retrofit.TeamProjectDTO
import com.example.spa_android.retrofit.UserModel
import com.example.spa_android.retrofit.UserRequestModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
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
    //유저 정보 (/api/user)
    @GET("/api/user/list") //회원 정보
    fun doGetUserList(): Call<ArrayList<UserModel>>

    @POST("/api/user/save") //회원가입
    fun saveUser(@Body user: UserRequestModel): Call<UserRequestModel>

    @Multipart
    @PUT("/api/user/update/{email}") //회원 정보 수정
    fun updateUser(@Path("email") email: String, @Part("fireDTO") user: RequestBody,@Part filePath: MultipartBody.Part)
    : Call<Map<String,String>>

    @DELETE("/api/user/delete/{email}") //회원 탈퇴
    fun deleteUser(@Path("email") email: String): Call<Void>


    //게시판 (/api/board)
    @GET("/api/board/list") //게시판 게시글
    fun getBoardList(): Call<ArrayList<BoardModel>>

    @Multipart
    @POST("/api/board/write") //게시판 글쓰기
    fun writeBoard(@Part("boardDTO") board: RequestBody,@Part filePath: MultipartBody.Part) : Call<Map<String,String>>

    @GET("/api/board/download/{id}")
    fun downloadBoardFile(@Path("id") id: String): Call<ResponseBody>

    @GET("/api/board/view/{id}")
    fun getBoardById(@Path("id") id : String): Call<BoardModel>

    @POST("/api/board/delete/{owner}")
    fun deleteBoard(@Path("owner") email: String, @Body request: Map<String, String>): Call<Map<String,String>>



    @POST("/api/chat/new")
    fun createNewChat(@Body chatRequestDTO: ChatRequestDTO): Call<Void>


    //채팅 관련
    @GET("/api/room/list/{email}")
    fun getRoomByEmail(@Path("email") email: String): Call<ArrayList<RoomListDTO>>

    @GET("/api/chatRoom/list/{roomId}")
    fun getRoomMessage(@Path("roomId") roomId: String): Call<List<MessageDTO>>

    @PUT("api/chatRoom/read/{roomId}/{receiver}")
    fun markMessageAsRead(@Path("roomId") roomId: String,@Path("receiver") receiver: String): Call<Void>

    @POST("/api/chatRoom/send")
    fun sendMessageToUser(@Body chatRoomDTO: ChatRoomDTO): Call<Void>

    @GET("/api/room/listData/{email}/{receiver}/{projectId}")
    fun getChatListData(@Path("email") email: String, @Path("receiver") receiver: String, @Path("projectId") projectId: String): Call<RoomListDTO>






    //프로젝트 (/api/team)
    @GET("/api/team/list/email/{email}")
    fun getProjectList(@Path("email") email: String): Call<ArrayList<ProjectListModel>>

    @GET("/api/team/list/{id}")
    fun getProjectListById(@Path("id") id: String): Call<TeamProjectDTO>

    //멤버의 상태 변경
    @PUT("/api/member/status/{email}")
    fun changeConditionsMember(@Path("email") email: String, @Body request: Map<String, String>): Call<Map<String,String>>

    //공지사항 작성
    @POST("/api/content/content_create/{teamProjectId}")
    fun writeProjectInformation(@Path("teamProjectId") teamProjectId: String, @Body contentDTO: ContentDTO): Call<Map<String,String>>

    @GET("/api/content/list/{teamProjectId}")
    fun getProjectInformation(@Path("teamProjectId") teamProjectId: String): Call<ArrayList<ProjectContentEntity>>

    //프로젝트 파일 관리
    @Multipart
    @POST("/api/content/upload/{teamProjectId}")
    fun uploadFile(@Path("teamProjectId") teamProjectId: String, @Part file: MultipartBody.Part,@Part("projectDTO") projectDTO: FileDTO): Call<Map<String,String>>

    @GET("/api/content/file/{teamProjectId}")
    fun getFileList(@Path("teamProjectId") teamProjectId: String): Call<ArrayList<ProjectContentEntity>>

    @GET("/api/content/download/{id}")
    fun downloadFile(@Path("id") id: String): Call<ResponseBody>

    @DELETE("/api/content/deleted/{id}")
    fun deleteContent(@Path("id") id: String): Call<Void>

    //신청자 관리( /api/apply)
    @POST("/api/apply/apply/{id}")
    fun applyToProject(@Path("id") id: String, @Body request: Map<String, String>): Call<Map<String, String>>

    @GET("/api/apply/applicants/{email}")
    fun getApplicantsByEmail(@Path("email") email: String): Call<ArrayList<ApplicantModel>>

    @POST("/api/apply/approve/{boardId}")
    fun acceptMember(@Path("boardId") boardId: String ,@Query("applicants") applicants: String): Call<Map<String,String>>

    @POST("/api/apply/reject/{boardId}")
    fun rejectMember(@Path("boardId") boardId: String, @Query("applicants") applicants: String): Call<Map<String,String>>

    @POST("/token/save")
    fun saveToken(@Body tokenDTO: TokenDTO): Call<Void>

    @POST("/fcm/sendData")
    fun sendData(@Body fcmDataDTO: FCMDataDTO): Call<Void>

    @POST("/fcm/send")
    fun sendNotificationData(@Body fcmDTO: FCMDTO): Call<Void>


}