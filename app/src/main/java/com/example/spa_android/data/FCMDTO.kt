package com.example.spa_android.data

import com.example.spa_android.data.FCMDataDTO.Message

data class FCMDTO (
    val message: Message
) {
    data class Message(
        val token: String?,
        val notification: Notification?,
        val data: Data
    )

    data class Notification(
        val title: String,
        val body: String
    )

    data class Data(
        val id: String?,
        val sender: String,
        val receiver: String,
        val chatName: String,
        val timestamp: String,
        val teamId: String,
        val partnerName: String,
        val roomId: String
    )
}