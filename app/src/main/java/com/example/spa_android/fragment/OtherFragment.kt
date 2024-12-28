package com.example.spa_android.fragment

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.spa_android.ApplicantActivity
import com.example.spa_android.DialogUtils
import com.example.spa_android.InformationActivity
import com.example.spa_android.LoginAndRegister
import com.example.spa_android.R
import com.example.spa_android.databinding.FragmentOtherBinding
import com.example.spa_android.retrofit.RetrofitApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtherFragment : Fragment() {
    private lateinit var binding: FragmentOtherBinding
    private lateinit var intent: Intent
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("MyInformation", Context.MODE_PRIVATE)
    }

    private fun logout() {
        with(sharedPreferences.edit()) {
            clear() // 모든 데이터를 삭제
            apply() // 변경사항을 저장
        }
        // Intent에 플래그를 추가하여 백 스택을 모두 제거
        intent = Intent(context, LoginAndRegister::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        startActivity(intent)
        requireActivity().finish() // 현재 Fragment가 포함된 Activity 종료

    }


    private val registerActivity: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult() // StartActivityForResult 처리를 담당
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val value = result.data?.getStringExtra("resultData")
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOtherBinding.inflate(inflater,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUserInformation()

        binding.logoutLinear.setOnClickListener {
            logout()
        }
        binding.myInfoConLayout.setOnClickListener {
            intent = Intent(context,InformationActivity::class.java)
            registerActivity.launch(intent)
        }

        binding.projectLinear.setOnClickListener {
            intent = Intent(context,ApplicantActivity::class.java)
            registerActivity.launch(intent) // 변경해야됌 일단 걸어논거
        }

        binding.deleteLinear.setOnClickListener {
            accountDelete()
        }
    }


    private fun accountDelete(){
        var email = sharedPreferences.getString("email",null)
        if(!email.isNullOrEmpty()) {
            RetrofitApplication.networkService.deleteUser(email).clone()?.enqueue(object :Callback<Void>{
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.d(TAG, "onResponse: ${response.body()}")
                    DialogUtils.showApplyDialog(context!!,"회원 탈퇴","회원 탈퇴 완료되었습니다."){
                        val intent = Intent(context,LoginAndRegister::class.java)
                        with(sharedPreferences.edit()) {
                            clear() // 모든 데이터를 삭제
                            apply() // 변경사항을 저장
                        }
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }

            })
        }
    }

    private fun updateUserInformation(){
        val filepath = sharedPreferences.getString("userprofile",null)
        binding.nameTv.text = sharedPreferences.getString("name",null)
        Glide.with(binding.userImageView.context)
            .load(RetrofitApplication.BASE_URL+ filepath)
            .error(R.drawable.sample_user)
            .into(binding.userImageView)
        Log.d(TAG, "updateUserInformation: ${RetrofitApplication.BASE_URL+filepath}")
    }
    

    companion object{
        const val TAG = "OtherFragment"
    }
}