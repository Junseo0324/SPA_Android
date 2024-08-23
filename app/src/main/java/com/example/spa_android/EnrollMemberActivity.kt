package com.example.spa_android


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityEnrollMemberBinding


class EnrollMemberActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEnrollMemberBinding
    private lateinit var intent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnrollMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.smBtn.setOnClickListener {
            //retrofit 코드가 존재해야함.
            DialogUtils.showApplyDialog(this,"신청","신청되었습니다."){
                finish()
            }
        }
        binding.backtoBoardBtn.setOnClickListener{
            finish()
        }

    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            val submitText = binding.submitEdit
            submitText.requestFocus()
            submitText.showSoftInputOnFocus = true
        }
    }

}