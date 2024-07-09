package com.example.spa_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.spa_android.fragment.BoardFragment

class EnrollMemberActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enroll_member)

        findViewById<Button>(R.id.smBtn).setOnClickListener {
            // BoardFragment로 이동하는 Intent 생성
            val intent = Intent(this, BoardFragment::class.java)
            startActivity(intent)
        }
    }
}