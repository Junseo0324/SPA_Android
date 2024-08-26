package com.example.spa_android

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityLoginAndRegisterBinding


class LoginAndRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginAndRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //로그인 회원가입 전환
        binding.loginText.setOnClickListener {
            binding.loginRecView.visibility = View.VISIBLE
            binding.loginText.setTextColor(Color.parseColor("#FFFFFF"))
            binding.registerText.setTextColor(Color.parseColor("#9E896A"))
            binding.registerRecView.visibility = View.INVISIBLE
            binding.rectangleEmail.visibility = View.GONE
            binding.tvEmail.visibility = View.GONE
            binding.editEmail.visibility = View.GONE
            binding.insertRec.text = "로그인"
        }
        binding.registerText.setOnClickListener {
            binding.loginRecView.visibility = View.INVISIBLE
            binding.loginText.setTextColor(Color.parseColor("#9E896A"))
            binding.registerText.setTextColor(Color.parseColor("#FFFFFF"))
            binding.registerRecView.visibility = View.VISIBLE
            binding.rectangleEmail.visibility = View.VISIBLE
            binding.tvEmail.visibility = View.VISIBLE
            binding.editEmail.visibility = View.VISIBLE
            binding.insertRec.text="회원가입"
        }


        //main으로 시작
        binding.insertRec.setOnClickListener {

            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}