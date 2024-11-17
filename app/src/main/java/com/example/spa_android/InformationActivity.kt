package com.example.spa_android

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.spa_android.databinding.ActivityInfomationBinding
import com.example.spa_android.retrofit.RetrofitApplication
import com.example.spa_android.retrofit.UserModel
import com.example.spa_android.retrofit.UserRequestModel
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class InformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfomationBinding
    private lateinit var changeUser: UserRequestModel
    private var selectedImageUri: Uri? = null
    private var userList = ArrayList<UserModel>()
    private val userCall: Call<ArrayList<UserModel>> = RetrofitApplication.networkService.doGetUserList()
    private val imageUrl = RetrofitApplication.BASE_URL
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfomationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MyInformation",Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email",null)
        if (email != null) {
            setUserInformation(email)
        }else{
            startActivity(Intent(this,LoginAndRegister::class.java))
        }

        binding.userInformationIcon.setOnClickListener{
            selectImageFromGallery()
        }

        binding.updateBtn.setOnClickListener {
            updateUserInfo()
        }
        binding.backBtn.setOnClickListener {
            finish()
        }
        SpinnerHandler.setUpSpinnerItem(this,binding)
        SpinnerHandler.setUpSpinnerListener(binding)
    }


    private fun selectImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val imageUri = result.data?.data
                selectedImageUri = result.data?.data // 선택된 이미지 URI를 멤버 변수에 저장

                // Glide를 사용해 이미지를 로드
                if (imageUri != null) {
                    Glide.with(this)
                        .load(imageUri)
                        .into(binding.userInformationIcon)
                } else {
                    Toast.makeText(this, "이미지를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun updateUserInfo(){
        val name = binding.editName.text.toString()
        val email = binding.editEmail3.text.toString()
        val phone = binding.editPhone.text.toString()
        val school = binding.editSchool.text.toString()
        val year = binding.spinner.selectedItem.toString()
        val major = binding.editMajor.text.toString()
        val password = binding.editPwd.text.toString()

        changeUser = UserRequestModel(
            filePath = selectedImageUri?.let { getPathFromUri(it) },
            name = name,
            password = password,
            email = email,
            phone_num = phone,
            school = school,
            year = year,
            major = major
        )
        Log.d("InformationActivity","changeUser: $changeUser")
        if (selectedImageUri != null) {
            // URI에서 파일 경로 가져오기
            val filePath = getPathFromUri(selectedImageUri!!)
            Log.d("InformationActivity", "selectedImageUri: $selectedImageUri")

            val file = File(filePath)

            // 파일 확장자와 MIME 타입 결정
            val fileName = "${email}.${if (file.extension == "png") "png" else "jpeg"}"
            Log.d("InformationActivity", "File extension: ${file.extension}")

            val mimeType = if (file.extension == "png") "image/png" else "image/jpeg"

            // MultipartBody.Part 생성
            val filePart = MultipartBody.Part.createFormData(
                "filepath", // 서버에서 기대하는 파라미터 이름
                fileName, // 파일 이름 (이메일.png 또는 이메일.jpeg)
                file.asRequestBody(mimeType.toMediaTypeOrNull()) // MIME 타입에 맞게 RequestBody 생성
            )

            // 사용자 정보 업데이트 호출
            updateUser(email, changeUser, filePart)
        } else {
            Log.d("InformationActivity", "이미지가 선택되지 않았습니다.")
        }
    }

    private fun updateUser(email: String,user: UserRequestModel,file:MultipartBody.Part){
        val json = Gson().toJson(user)
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
        RetrofitApplication.networkService.updateUser(email,requestBody,file).enqueue(object : Callback<Map<String,String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    Log.d("InformationActivity", "회원정보 업데이트 성공: ${response.body()}")
                    DialogUtils.showApplyDialog(this@InformationActivity, "수정", "수정되었습니다.") {
                        finish()
                    }
                } else {
                    Log.d("InformationActivity", "회원정보 업데이트 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.d("InformationActivity", "회원정보 업데이트 실패: ${t.message}")
            }
        })
    }

    private fun getPathFromUri(uri: Uri): String? {
        Log.d("InformationActivity","getPathFromUri 시작")
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(cacheDir, "temp_image.png")
        Log.d("InformationActivity",uri.toString())
        Log.d("InformationActivity",file.absolutePath)
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath
    }

    private fun setUserInformation(userEmail: String){
        userCall.clone()?.enqueue(object : Callback<ArrayList<UserModel>>{
            override fun onResponse(call: Call<ArrayList<UserModel>>, response: Response<ArrayList<UserModel>>) {
                if(response.isSuccessful){
                    userList = response.body()?.filter { it.email == userEmail } as ArrayList<UserModel>
                    binding.editName.setText(userList[0].name)
                    binding.editEmail3.setText(userList[0].email)
                    binding.editPhone.setText(userList[0].phone_num)
                    binding.editSchool.setText(userList[0].school)
                    binding.editMajor.setText(userList[0].major)
                    binding.editPwd.setText(userList[0].password)
                    SpinnerHandler.setSelectedItem(binding,userList[0].year)
                    Glide.with(binding.userInformationIcon.context)
                        .load(imageUrl+userList[0].filePath)
                        .error(R.drawable.sample_user)
                        .into(binding.userInformationIcon)
                }
            }

            override fun onFailure(call: Call<ArrayList<UserModel>>, t: Throwable) {
                Log.d("InformationActivity","실패 ${t.message}")
            }
        })
    }


}