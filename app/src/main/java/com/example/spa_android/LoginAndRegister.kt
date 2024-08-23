package com.example.spa_android

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityLoginAndRegisterBinding


class LoginAndRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginAndRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putString("usermail", "사용자 이메일")
        editor.putString("password", "비밀번호")
        editor.apply() // 변경 사항을 저장

        val usermail = sharedPreferences.getString("usermail", null)
        val password = sharedPreferences.getString("password", null)

        if (usermail != null && password != null) {
            // 로그인 정보가 존재할 때 처리
            println("사용자 이메일: $usermail")
            println("비밀번호: $password")
        } else {
            // 로그인 정보가 없을 때 처리
            println("로그인 정보가 없습니다.")
        }

        val editorRemove = sharedPreferences.edit()
        editor.remove("usermail")
        editor.remove("password")
        editor.apply() // 변경 사항을 저장

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