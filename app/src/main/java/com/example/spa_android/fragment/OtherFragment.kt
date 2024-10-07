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
    private lateinit var name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("MyInformation", Context.MODE_PRIVATE)
    }

    private fun logout() {
        with(sharedPreferences.edit()) {
            clear() // 모든 데이터를 삭제
            apply() // 변경사항을 저장
        }
    }


    private val registerActivity: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult() // ◀ StartActivityForResult 처리를 담당
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val value = result.data?.getStringExtra("resultData")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtherBinding.inflate(inflater,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUserInformation()
        binding.logoutBtn.setOnClickListener{
            logout()
            intent = Intent(context,LoginAndRegister::class.java)

        }
        binding.myInfoConLayout.setOnClickListener {
            intent = Intent(context,InformationActivity::class.java)
            registerActivity.launch(intent)
        }

        binding.projectLinear.setOnClickListener {
            intent = Intent(context,ApplicantActivity::class.java)
            registerActivity.launch(intent) // 변경해야됌 일단 걸어논거
        }

        binding.deleteBtn.setOnClickListener {
            accountDelete()
        }
    }

    private fun accountDelete() {
        var userId = sharedPreferences.getLong("id", 0)
        RetrofitApplication.networkService.deleteUser(userId).enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    DialogUtils.showApplyDialog(requireContext(),"탈퇴","탈퇴 처리가 완료되었습니다."){
                        intent = Intent(context,LoginAndRegister::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) //백스택 액티비티 초기화
                        startActivity(intent)
                    }
                }
            }


            override fun onFailure(call: Call<Void>, t: Throwable) {

            }

        })

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