package com.example.spa_android

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.spa_android.databinding.ActivityWriteBoradBinding
import com.example.spa_android.retrofit.BoardRequestModel
import com.example.spa_android.retrofit.RetrofitApplication
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream


class WriteBoardActivity : AppCompatActivity() {
    private var selectedFilePath: Uri? = null
    private var selectedFileName: String? = null
    private lateinit var binding: ActivityWriteBoradBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var writeBoardModel: BoardRequestModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityWriteBoradBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUserInformation()
        // 버튼 클릭 이벤트 리스너 설정
        binding.writeSubmit.setOnClickListener {
            Log.d(TAG,"submitBtn 클릭")
            sendWrite()
        }
        binding.deletefileBtn.setOnClickListener {
            clearSelectedFile()
        }
        binding.fileupBtn.setOnClickListener{
            openFileChooser()
        }

        setSupportActionBar(binding.toolbarWriteBoard)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back)


        updateDeleteBtnVisibility()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun clearSelectedFile() {
        // 선택된 파일 초기화
        selectedFilePath = null
        selectedFileName = null
        binding.fileupBtn.text = "파일 선택" // 기본 텍스트로 초기화
        updateDeleteBtnVisibility()
    }

    private fun updateDeleteBtnVisibility() {
        if (selectedFilePath != null) {
            binding.deletefileBtn.visibility = View.VISIBLE // 파일이 첨부되면 보이기
        } else {
            binding.deletefileBtn.visibility = View.GONE // 파일이 없으면 숨기기
        }
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*" // 모든 파일 형식 선택 가능
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        filePickerLauncher.launch(intent)
    }

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            selectedFilePath = result.data?.data //선택된 이미지의 URI를 가져옴
            Log.d(TAG, "선택된 파일 URI: $selectedFilePath")
            result.data?.data?.let { uri ->
                handleFileUpload(uri)
            }
        }
    }

    private fun handleFileUpload(uri: Uri) {
        // 파일 경로와 이름 저장
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                selectedFileName = cursor.getString(nameIndex) // 파일 이름 저장
                binding.fileupBtn.text = selectedFileName
            }
        }
        updateDeleteBtnVisibility()
    }

    private fun setUserInformation(){
        sharedPreferences = getSharedPreferences("MyInformation", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("name",null)
        val filePath = sharedPreferences.getString("filePath",null)
        binding.nameTv2.text = name
        Glide.with(binding.userIcon.context)
            .load(RetrofitApplication.BASE_URL+filePath)
            .error(R.drawable.sample_user)
            .into(binding.userIcon)

    }


    private fun sendWrite(){
        val title = binding.titleEdit.text.toString()
        val content = binding.contentEdit.text.toString()
        val owner = sharedPreferences.getString("email","관리자")

        writeBoardModel = owner?.let { BoardRequestModel(title,content, it) }!!

        // URI에서 실제 파일 경로 가져오기
        selectedFilePath?.let { uri ->
            val inputStream = contentResolver.openInputStream(uri)
            val file = File.createTempFile("upload", selectedFileName) // 임시 파일 생성
            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }

            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("filePath", selectedFileName, requestFile)

            // 글쓰기 요청
            writeBoard(writeBoardModel, filePart)
        } ?: Log.d(TAG, "선택된 파일이 없습니다.")
    }

    private fun writeBoard(board :BoardRequestModel, file:MultipartBody.Part){
        val json = Gson().toJson(board)
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
        RetrofitApplication.networkService.writeBoard(requestBody,file).enqueue(object :
            Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    DialogUtils.showApplyDialog(this@WriteBoardActivity,"신청","신청되었습니다."){
                        finish()
                    }
                } else {
                    Log.d(TAG, "글쓰기 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.d(TAG, "글쓰기 실패: ${t.message}")
            }
        })
    }


    companion object{

        const val TAG = "WriteBoardActivity"
    }

}