package com.example.spa_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityProjectFileBinding

class ProjectFileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProjectFileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectFileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.uploadFileBtn.setOnClickListener {
            //파일 업로드
        }
        binding.projectFileBtn.setOnClickListener {
            DialogUtils.showApplyDialog(this,"저장","저장되었습니다."){
                finish()
            }
        }

    }

}