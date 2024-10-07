package com.example.spa_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityInsertInfoBinding

class InsertInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInsertInfoBinding
    private lateinit var intent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isEdit = intent.getBooleanExtra("isEdit", false)
        val insertInfoBtn = findViewById<Button>(R.id.insertInfoBtn)
        if(isEdit) {
            insertInfoBtn.text = "수정하기"
        } else {
            insertInfoBtn.text = "작성하기"
        }



        binding.insertInfoBtn.setOnClickListener {
            intent = Intent(this,ProjectActivity::class.java)
            DialogUtils.showApplyDialog(this,"수정","수정되었습니다.") {
                finish()
            }
        }
    }
}