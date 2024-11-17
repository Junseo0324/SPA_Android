package com.example.spa_android

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityInsertInfoBinding
import com.example.spa_android.retrofit.ContentDTO
import com.example.spa_android.retrofit.RetrofitApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInsertInfoBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyInformation",Context.MODE_PRIVATE)

        val isEdit = intent.getBooleanExtra("isEdit", false)
        if(isEdit) {
            binding.insertInfoBtn.text = "수정하기"
        } else {
            binding.insertInfoBtn.text = "작성하기"
        }



        binding.insertInfoBtn.setOnClickListener {
            Log.d(TAG, "onCreate: ${intent.getStringExtra("projectId")}")
            writeInformation(intent.getStringExtra("projectId").toString())
            DialogUtils.showApplyDialog(this,"작성","공지사항이 작성되었습니다.") {
                finish()
            }
        }
    }

    private fun createInformationData() : ContentDTO{
        var content = ContentDTO(
            author = sharedPreferences.getString("email",null).toString(),
            title = binding.infoTitleEdit.text.toString(),
            content = binding.infoContentEdit.text.toString()
        )
        Log.d(TAG, "createInformationData: $content")
        return content
    }

    private fun writeInformation(id: String) {
        var contentDTO = createInformationData()
        RetrofitApplication.networkService.writeProjectInformation(id,contentDTO).clone()?.enqueue(object : Callback<Map<String,String>>{
            override fun onResponse(call: Call<Map<String,String>>, response: Response<Map<String,String>>) {
                if(response.isSuccessful){
                    Log.d(TAG, "onResponse: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<Map<String,String>>, t: Throwable) {
                Log.d(TAG, "writeProjectInformation onFailure: ${t.message}")
            }

        })
    }
    
    
    companion object {
        const val TAG= "InsertInfoActivity"
    }
}