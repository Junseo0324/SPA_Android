package com.example.spa_android.retrofit

data class ChatModel(
    var id: Long,
    var sender: String, //송신자
    var receiver: String, //수신자
    var message: String, //전송 메시지
    var timestamp: String, //전송 시간
    var chatName: String, //채팅방 이름
    var teamId: String, //프로젝트 이름
    var isRead: Boolean,
    var count: Int = 0//메시지 읽음 여부

)

data class ChatRequestModel(
    var sender: String,
    var receiver: String, //수신자
    var message: String, //전송 메시지
    var chatName: String, //채팅방 이름
    var teamId: String //프로젝트 이름
)

data class ChatSummaryDTO(
    var sender: String,
    var receiver: String,
    var chatName: String,
    var latestMessage: String,
    var senderName: String,
    var timestamp: String,
    var unreadCount: Int,
    var filePath : String,
    var partner : String,
    var partnerEmail: String
)