package com.example.spa_android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityWriteBoradBinding

class WriteBoradActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteBoradBinding
    private lateinit var intent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteBoradBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }
        // 버튼 클릭 이벤트 리스너 설정
        binding.writeSubmit.setOnClickListener {
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
                finish()
            }
            .show()

    }
}