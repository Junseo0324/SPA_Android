package com.example.spa_android

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BoardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        // UI 요소와 연결
        val tvUserId: TextView = findViewById(R.id.tvUserId)
        val imgPost: ImageView = findViewById(R.id.imgPost)
        val tvTitle: TextView = findViewById(R.id.tvTitle)
        val tvContent: TextView = findViewById(R.id.tvContent)
        val btnAttachment: Button = findViewById(R.id.btnAttachment)
        val tvTimestamp: TextView = findViewById(R.id.tvTimestamp)

        // Intent로 전달된 데이터 받기
        val userId = intent.getStringExtra("userId")
        val imageResId = intent.getIntExtra("imageResId", R.drawable.ic_launcher_background)
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val timestamp = intent.getStringExtra("timestamp")

        // 데이터를 UI에 반영
        tvUserId.text = userId
        imgPost.setImageResource(imageResId)
        tvTitle.text = title
        tvContent.text = content
        tvTimestamp.text = timestamp

        // 첨부 파일 버튼 클릭 시 동작
        btnAttachment.setOnClickListener {
            Toast.makeText(this, "첨부된 파일을 확인하세요.", Toast.LENGTH_SHORT).show()
            // 첨부 파일 열기 로직 추가 가능
        }
    }
}