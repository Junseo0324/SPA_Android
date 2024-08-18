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
//            intent = Intent(this,BoardFragment::class.java)
            //이 사이에 값이 들어가는 메소드 존재해야한다.
            finish()
        }
        binding.backtoBoardBtn.setOnClickListener{
//            intent = Intent(this,BoardFragment::class.java)
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
