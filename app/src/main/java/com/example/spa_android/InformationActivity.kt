package com.example.spa_android

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.LoginAndRegister.Companion.TAG
import com.example.spa_android.databinding.ActivityInfomationBinding
import com.example.spa_android.retrofit.RegisterUserModel
import com.example.spa_android.retrofit.RetrofitApplication
import com.example.spa_android.retrofit.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfomationBinding
    private lateinit var changeUser: RegisterUserModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfomationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.informationImageTv.setOnClickListener{
            //이미지를 내 폰 파일에서 가져와서 서버로 보낼 준비를 하고 이미지를 바꿈
        }

        binding.updateBtn.setOnClickListener {
            val name = binding.editName.text
            val email = binding.editEmail3.text
            val phone = binding.editPhone.text
            val school = binding.editSchool.text
            val year = binding.spinner.selectedItem.toString()
            val major = binding.editMajor.text
//            changeUser = RegisterUserModel(name,email,phone,school,year,major)
            DialogUtils.showApplyDialog(this,"수정","수정되었습니다."){
                finish()
            }
        }
        binding.backBtn.setOnClickListener {
            finish()
        }

        setUpSpinnerItem()
        setUpSpinnerHandler()
    }




    private fun setUpSpinnerItem(){
        val spinnerItem = resources.getStringArray(R.array.spinner_item)
        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,spinnerItem)
        binding.spinner.adapter =adapter
    }
    private fun setUpSpinnerHandler(){
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //item이 선택되었을 때
               // binding.tvYear.text = "select : ${binding.spinner.getItemAtPosition(position)}"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun updateUser(email: String,updateData: UserModel){
        RetrofitApplication.networkService.updateUser(email,updateData).enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                //업데이트 성공 시
            }
            override fun onFailure(call: Call<UserModel>, t: Throwable) {

                Log.d(TAG,"회원가입 실패 ${t.message}")
            }
        })
    }
}