package com.example.spa_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.spa_android.fragment.BoardFragment

class WriteBoradActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_borad)

        val applyButton: Button = findViewById(R.id.writeSubmit)

        findViewById<Button>(R.id.writeSubmit).setOnClickListener {
            // BoardFragment로 이동하는 Intent 생성
            val intent = Intent(this, BoardFragment::class.java)
            startActivity(intent)
        }
        // 버튼 클릭 이벤트 리스너 설정
        applyButton.setOnClickListener {
            showApplyDialog()
        }
    }

    // 다이얼로그를 표시
    private fun showApplyDialog() {
        AlertDialog.Builder(this)
            .setTitle("신청")
            .setMessage("신청되었습니다.")
            .setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

    }
}