package com.example.spa_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityWriteBoradBinding

class WriteBoradActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteBoradBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteBoradBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }
        // 버튼 클릭 이벤트 리스너 설정
        binding.writeSubmit.setOnClickListener {
            DialogUtils.showApplyDialog(this,"신청","신청되었습니다."){
                finish()
            }
        }
    }



}