package com.example.spa_android

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spa_android.Adapter.ApplicantAdapter
import com.example.spa_android.databinding.ActivityApplicantBinding
import com.example.spa_android.retrofit.ApplicantModel
import com.example.spa_android.retrofit.RetrofitApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplicantActivity : AppCompatActivity() , OnApplicantActionListener{
    private lateinit var binding: ActivityApplicantBinding
    private lateinit var adapter: ApplicantAdapter
    private var applicantModel : ArrayList<ApplicantModel> = ArrayList()
    var email = "default"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplicantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var sharedPreferences = getSharedPreferences("MyInformation",Context.MODE_PRIVATE)
        email = sharedPreferences.getString("email",null).toString()
        var applicantList = getApplicantsList(email.toString())
        adapter = ApplicantAdapter(applicantList,this)
        binding.applicantRv.adapter = adapter
        binding.applicantRv.layoutManager = LinearLayoutManager(this)

        // 뒤로 가기 버튼 클릭 시 Activity 종료
        binding.backBtn.setOnClickListener {
            finish()
        }

    }

    private fun getApplicantsList(email: String) : ArrayList<ApplicantModel>{
        RetrofitApplication.networkService.getApplicantsByEmail(email).clone()?.enqueue(object :Callback<ArrayList<ApplicantModel>>{
            override fun onResponse(call: Call<ArrayList<ApplicantModel>>, response: Response<ArrayList<ApplicantModel>>) {
                if(response.isSuccessful){
                    applicantModel.clear()
                    applicantModel.addAll(response.body()!!)
                    adapter.notifyDataSetChanged() // 어댑터에 데이터 변경 알림
                }else{
                    Log.d(TAG, "onResponse: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<ApplicantModel>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
        return applicantModel
    }

    companion object{
        const val TAG="ApplicantActivity"
    }

    override fun onAccept(applicant: ApplicantModel) {
        RetrofitApplication.networkService.acceptMember(applicant.boardId, applicant.applicants).clone()?.enqueue(object : Callback<Map<String,String>>{
            override fun onResponse(call: Call<Map<String,String>>, response: Response<Map<String,String>>) {
                if(response.isSuccessful){
                    Log.d(TAG, "onResponse: ${response.body()}")
                    DialogUtils.showApplyDialog(this@ApplicantActivity,"승인","승인되었습니다."){
                        getApplicantsList(email)
                    }
                }
            }

            override fun onFailure(call: Call<Map<String,String>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    override fun onReject(applicant: ApplicantModel) {
        RetrofitApplication.networkService.rejectMember(applicant.boardId, applicant.applicants).clone()?.enqueue(object : Callback<Map<String,String>>{
            override fun onResponse(call: Call<Map<String,String>>, response: Response<Map<String,String>>) {
                if(response.isSuccessful){
                    Log.d(TAG, "onResponse: ${response.message()}")
                    DialogUtils.showApplyDialog(this@ApplicantActivity,"거절","거절되었습니다."){
                        getApplicantsList(email)
                    }
                }
            }

            override fun onFailure(call: Call<Map<String,String>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }
}