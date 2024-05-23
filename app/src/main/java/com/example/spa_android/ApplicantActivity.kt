package com.example.spa_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityApplicantBinding

class ApplicantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityApplicantBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}