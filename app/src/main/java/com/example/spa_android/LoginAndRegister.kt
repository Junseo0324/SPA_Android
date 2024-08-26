package com.example.spa_android

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityLoginAndRegisterBinding
import com.example.spa_android.retrofit.RegisterUserModel
import com.example.spa_android.retrofit.RetrofitApplication
import com.example.spa_android.retrofit.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginAndRegister : AppCompatActivity() {
    var userList = ArrayList<UserModel>()
    val userCall: Call<ArrayList<UserModel>> = RetrofitApplication.networkService.doGetUserList()
    private lateinit var binding: ActivityLoginAndRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAndRegisterBinding.inflate(layoutInflater)
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

            if(binding.insertRec.text.equals("로그인")){
                getUserList()
            }
            else{ //register
                val name = binding.editId.text.toString()
                val password = binding.editPw.text.toString()
                val email = binding.editEmail.text.toString()

                if(name.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()){
                    registerUser(name,password,email)
                }
                else{
                    val message = when {
                        name.isEmpty() -> "ID를 채워주세요"
                        password.isEmpty() -> "비밀번호를 채워주세요"
                        else -> "이메일을 채워주세요"
                    }
                    if (message.isNotEmpty()) {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    private fun getUserList(){
        userCall.clone()?.enqueue(object : Callback<ArrayList<UserModel>>{
            override fun onResponse(call: Call<ArrayList<UserModel>>, response: Response<ArrayList<UserModel>>) {
                if(response.isSuccessful){
                    Log.d(TAG,response.body().toString())
                    userList = response.body()!!

                    checkLogin(binding.editId.text.toString(),binding.editPw.text.toString())
                }
            }

            override fun onFailure(call: Call<ArrayList<UserModel>>, t: Throwable) {
                Log.d(TAG,"실패 ${t.message}")
            }
        })
    }

    private fun registerUser(name: String, password: String, email: String){
        val temp = "X"
        val newUser = RegisterUserModel(name,password,email,temp,temp,temp,temp)
        RetrofitApplication.networkService.saveUser(newUser).enqueue(object : Callback<RegisterUserModel>{
            override fun onResponse(call: Call<RegisterUserModel>, response: Response<RegisterUserModel>) {
                Toast.makeText(this@LoginAndRegister,"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<RegisterUserModel>, t: Throwable) {

                Log.d(TAG,"회원가입 실패 ${t.message}")
            }
        })
    }

    private fun checkLogin(inputId: String, inputPw: String){
        val user = userList.find { it.name == inputId && it.password == inputPw }
        Log.d(TAG,user.toString())
        if(user != null){

            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this,"로그인 실패! 아이디와 비번 확인",Toast.LENGTH_SHORT).show()
        }
    }

    companion object{
        const val TAG = "LoginAndRegister"
    }
}