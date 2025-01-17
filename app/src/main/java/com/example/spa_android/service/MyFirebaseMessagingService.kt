package com.example.spa_android.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.spa_android.MessgeActivity
import com.example.spa_android.R
import com.example.spa_android.data.TokenDTO
import com.example.spa_android.retrofit.RetrofitApplication
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("MyInformation",Context.MODE_PRIVATE)
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // 서버에 토큰 전송 로직
        sendTokenToServer(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("onMessage", message.data.toString())
        Log.d("onMessageReceived", "Message data: ${message.data}")
        Log.d("onMessageReceived", "Message notification: ${message.notification}")

        var title = "알림"
        var body = "내용 없음"
        // 알림 메시지 처리
        val notification = message.notification
        if (notification != null) {
            title = notification.title ?: "알림"
            body = notification.body ?: "내용 없음"
            val data = message.data
            val sender = data["sender"]
            val chatName = data["chatName"]
            val projectID = data["teamId"]
            val roomId = data["roomId"]
            val partner = data["partnerName"]
            Log.d("firebase", "onMessageReceived: $data")
//            showNotification(title, body)
            showDataNotification(title,
                body,
                sender.toString(),
                sharedPreferences.getString("email",null).toString(),
                projectID.toString(),
                roomId.toString(),
                partner.toString(),
                chatName.toString())
        }

//        // 데이터 메시지 처리
//        if (message.data.isNotEmpty()) {
//            val data = message.data
//            val sender = data["sender"]
//            val chatName = data["chatName"]
//            val projectID = data["teamId"]
//            val roomId = data["roomId"]
//            val partner = data["partnerName"]
//            Log.d("firebase", "onMessageReceived: $data")
//            showDataNotification(title,
//                body,
//                sender.toString(),
//                sharedPreferences.getString("email",null).toString(),
//                projectID.toString(),
//                roomId.toString(),
//                partner.toString(),
//                chatName.toString())
//        }

    }

    private fun sendTokenToServer(token: String) {
        val sharedPreferences = getSharedPreferences("MyInformation", MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)

        if (email != null) {
            val tokenDTO = TokenDTO(email, token)
            RetrofitApplication.networkService.saveToken(tokenDTO).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("FCM", "Token successfully sent to server: $tokenDTO")
                    } else {
                        Log.e("FCM", "Failed to send token: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("FCM", "Error sending token to server: ${t.message}")
                }
            })
        } else {
            Log.e("FCM", "Email not found in SharedPreferences. Token not sent.")
        }
    }

    private fun showDataNotification(title: String, body: String,sender: String, receiver: String, projectId: String,roomId:String,partner:String,chatName:String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val intent = Intent(this, MessgeActivity::class.java)
            intent.putExtra("chatName", chatName)
            intent.putExtra("roomId", roomId)
            intent.putExtra("myEmail", receiver)
            intent.putExtra("projectNumber", projectId)
            intent.putExtra("partner", partner)
            intent.putExtra("partnerEmail",sender)

            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channelId = "default_channel"
                val channel = NotificationChannel(
                    channelId,
                    "Default Channel",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)

                val notification = NotificationCompat.Builder(this, channelId)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build()

                notificationManager.notify(0, notification)
            } else {
                val notification = NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build()

                notificationManager.notify(0, notification)
            }

    }
    private fun showNotification(title: String, body: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, MessgeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channelId = "default_channel"
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(this, channelId)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(0, notification)
        } else {
            val notification = NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(0, notification)
        }
    }
}