package com.example.spa_android.data

import com.google.gson.annotations.SerializedName

data class ChatingItem(
    val name : String, //보낸 사람 sender
    val message: String, //메시지
    @SerializedName("type") // JSON에서 "type"으로 매핑
    val state : Int, //나 인지 아닌지 // 0 or 1
    val chatName: String
)
