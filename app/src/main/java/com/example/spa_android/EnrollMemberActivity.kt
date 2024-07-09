package com.example.spa_android


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.spa_android.fragment.BoardFragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityEnrollMemberBinding


class EnrollMemberActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEnrollMemberBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


     // BoardFragment로 이동하는 Intent 생성
           

        binding = ActivityEnrollMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

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