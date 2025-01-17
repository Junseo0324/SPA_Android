package com.example.spa_android.data

data class FCMDataDTO(
    val message: Message
) {
    data class Message(
        val token: String?,
        val data: Data
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
