package com.example.spa_android.data

data class MessageDTO(
    var sender: String,
    var receiver: String,
    var senderName: String,
    var receiverName: String,
    var message: String,
    var isRead: Boolean
)
