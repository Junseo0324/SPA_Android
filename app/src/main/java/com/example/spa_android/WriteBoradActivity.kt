package com.example.spa_android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityWriteBoradBinding
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class WriteBoradActivity : AppCompatActivity() {
    private lateinit var fileupBtn: Button
    private var selectedFilePath: String? = null
    private var selectedFileName: String? = null
    private lateinit var binding: ActivityWriteBoradBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityWriteBoradBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()

        }
        // 버튼 클릭 이벤트 리스너 설정
        binding.writeSubmit.setOnClickListener {
            DialogUtils.showApplyDialog(this,"신청","신청되었습니다."){
                finish()
            }
        }
        fileupBtn = findViewById(R.id.fileupBtn)
        fileupBtn.setOnClickListener{
            openFileChooser()
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
            result.data?.data?.let { uri ->
                handleFileUpload(uri)
            }
        } else {
            Toast.makeText(this, "파일 선택이 취소되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun handleFileUpload(uri: Uri) {
        // 파일 경로와 이름 저장
        selectedFilePath = uri.toString() // URI를 문자열로 변환하여 저장
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                selectedFileName = cursor.getString(nameIndex) // 파일 이름 저장
                Toast.makeText(this, "선택한 파일: $selectedFileName", Toast.LENGTH_SHORT).show()
            }
        }
    }

}