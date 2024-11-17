package com.example.spa_android


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityEnrollMemberBinding
import com.example.spa_android.retrofit.BoardModel
import com.example.spa_android.retrofit.RetrofitApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EnrollMemberActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEnrollMemberBinding
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnrollMemberBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences("MyInformation", Context.MODE_PRIVATE)
        setContentView(binding.root)
        binding.smBtn.setOnClickListener {
            applicationProject()
        }
        binding.backtoBoardBtn.setOnClickListener{
            finish()
        }

    }

    private fun applicationProject(){
        val boardItem = intent.getSerializableExtra("board_item") as? BoardModel
        val boardId = boardItem?.id.toString()
        Log.d(TAG, "applicationProject: boardID $boardId")
        var data = getapplicationData()
        Log.d(TAG, "applicationProject: data $data")
        if(data!=null) {
            RetrofitApplication.networkService.applyToProject(boardId, data).clone()?.enqueue(object :Callback<Map<String,String>>{
                override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                    Log.d(TAG, "onResponse: ${response.body()}")
                    DialogUtils.showApplyDialog(this@EnrollMemberActivity,"신청","신청되었습니다."){
                        finish()
                    }
                }

                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }

            })
        }
    }

    private fun getapplicationData() : Map<String, String> {
        val data = mutableMapOf<String, String>()
        data["applicants"] = sharedPreferences.getString("email", null).toString() // 키 이름 수정
        data["applied_motive"] = binding.submitEdit.text.toString()
        data["role"]=binding.roleEdit.text.toString()
        return data
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            val submitText = binding.submitEdit
            submitText.requestFocus()
            submitText.showSoftInputOnFocus = true
        }
    }

    companion object{
        const val TAG="EnrollMemberActivity"
    }
}