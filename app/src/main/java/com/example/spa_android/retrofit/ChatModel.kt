package com.example.spa_android.retrofit

data class ChatModel(
    var id: Long,
    var sender: String, //송신자
    var receiver: String, //수신자
    var message: String, //전송 메시지
    var timestamp: String, //전송 시간
    var chatName: String //채팅방 이름
)
