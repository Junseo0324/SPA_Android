package com.example.spa_android

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityProjectFileBinding
import com.example.spa_android.retrofit.FileDTO
import com.example.spa_android.retrofit.RetrofitApplication
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ProjectFileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProjectFileBinding
    private var selectedFilePath: Uri? = null
    private var selectedFileName: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("MyInformation",Context.MODE_PRIVATE)

        val isEdit = intent.getBooleanExtra("isEdit", false)
        if (isEdit) {
            binding.projectFileBtn.text = "수정"
        } else {
            binding.projectFileBtn.text = "저장"
        }
        binding.uploadFileBtn.setOnClickListener {
            openFileChooser()
        }
        binding.projectFileBtn.setOnClickListener {
            DialogUtils.showApplyDialog(this,"저장","저장되었습니다."){
                sendToFile(intent.getStringExtra("projectId").toString())
                finish()
            }
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
            Log.d(WriteBoardActivity.TAG, "선택된 파일 URI: $selectedFilePath")
            result.data?.data?.let { uri ->
                handleFileUpload(uri)
            }
        } else {
            Toast.makeText(this, "파일 선택이 취소되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun handleFileUpload(uri: Uri) {
        // 파일 경로와 이름 저장
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                selectedFileName = cursor.getString(nameIndex) // 파일 이름 저장
                binding.uploadfileName.text = selectedFileName
            }
        }
    }


    private fun sendToFile(id: String) {
        if (selectedFilePath == null) {
            Toast.makeText(this, "파일을 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // 파일 URI에서 RequestBody 및 MultipartBody.Part 생성
        val fileDescriptor = contentResolver.openFileDescriptor(selectedFilePath!!, "r") ?: return
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val file = File(cacheDir, selectedFileName ?: "tempFile")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()

        // RequestBody 및 MultipartBody.Part 생성
        val requestFile = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

        // FileDTO 데이터를 생성
        var projectDTO = setDtoData()
        Log.d(TAG, "sendToFile: $projectDTO")
        RetrofitApplication.networkService.uploadFile(id,filePart,projectDTO).clone()?.enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(call: Call<Map<String,String>>, response: Response<Map<String, String>>) {
                if(response.isSuccessful){
                    Log.d(TAG, "onResponse: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<Map<String,String>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setDtoData() :FileDTO {
        var data = FileDTO(
            author = sharedPreferences.getString("email",null).toString(),
            description = binding.fileContentEdit.text.toString()
        )
        return data
    }




    companion object {
        const val TAG="ProjectFileActivity"
    }

}