package com.example.spa_android.data

data class RoomListDTO(
    var myEmail: String,
    var myName: String,
    var partner: String,
    var partnerName: String,
    var roomId: String,
    var projectId: String,
    var filepath: String,
    var chatName: String,
    var lastMessage: String,
    var unReadCount: Long,
    var timestamp: String
)
