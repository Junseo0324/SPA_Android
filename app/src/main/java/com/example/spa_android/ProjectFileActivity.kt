package com.example.spa_android

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityProjectFileBinding

class ProjectFileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProjectFileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectFileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isEdit = intent.getBooleanExtra("isEdit", false)
        val projectFileBtn = findViewById<Button>(R.id.projectFileBtn)
        if (isEdit) {
            projectFileBtn.text = "수정"
        } else {
            projectFileBtn.text = "저장"
        }
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