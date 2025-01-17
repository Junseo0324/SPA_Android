package com.example.spa_android

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityBoardBinding
import com.example.spa_android.retrofit.BoardModel
import com.example.spa_android.retrofit.RetrofitApplication
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class BoardActivity : AppCompatActivity() {

    lateinit var binding: ActivityBoardBinding
    private var boardId: String? = null
    var name: String? = null
    var fileName: String? =null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPreferences = getSharedPreferences("MyInformation",Context.MODE_PRIVATE)
        val boardItem = intent.getSerializableExtra("board_item") as? BoardModel
        boardId = boardItem?.id.toString()
        name = boardItem?.name
        fileName  = boardItem?.filePath
        getBoardItem(boardId.toString())

        setSupportActionBar(binding.boardToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back)

        //파일 다운로드
        binding.mBtnAttachment.setOnClickListener {
            downloadFile(boardId.toString())
        }
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


    private fun setBoardItem(boardItem: BoardModel){
        binding.tvUserId.text = name.toString() ?: "Unknown"
        binding.tvTitle.text = boardItem.title//제목
        binding.tvContent.text = boardItem.content
        binding.tvTimestamp.text = boardItem.timestamp
    }

    private fun getBoardItem(id: String){
        RetrofitApplication.networkService.getBoardById(id).clone()?.enqueue(object :Callback<BoardModel>{
            override fun onResponse(call: Call<BoardModel>, response: Response<BoardModel>) {
                if(response.isSuccessful) {
                    var boardItem = response.body()!!
                    setBoardItem(boardItem)
                }
            }

            override fun onFailure(call: Call<BoardModel>, t: Throwable) {
                Log.d(TAG, "onFailure: getBoardItemByID 실패 : $t")
            }

        })
    }

    private fun downloadFile(id: String) {
        RetrofitApplication.networkService.downloadBoardFile(id).clone()?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        // MIME 타입 확인
                        val mimeType = response.headers()["Content-Type"]
                        val extension = getFileExtensionFromMimeType(mimeType)

                        // 파일 이름 생성
                        val fileName = "SPA_${sharedPreferences.getString("email", "default")}_${boardId}.$extension"
                        Log.d(TAG, "다운로드한 파일 이름: $fileName")

                        // 파일 저장
                        val success = saveFileToDisk(responseBody, fileName)
                        if (success) {
                            Toast.makeText(this@BoardActivity, "파일 다운로드 완료", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.d(TAG, "onResponse: 파일 다운로드 실패 : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun getFileExtensionFromMimeType(mimeType: String?): String {
        return when (mimeType) {
            "application/pdf" -> "pdf"
            "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> "pptx"
            "application/haansofthwp" -> "hwp"
            "image/jpeg" -> "jpg"
            "image/png" -> "png"
            else -> "bin" // 알 수 없는 타입의 기본 확장자
        }
    }




    private fun saveFileToDisk(body: ResponseBody, fileName: String): Boolean {
        return try {
            // 파일을 공용 Downloads 디렉터리에 저장
            val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)

            // 파일 쓰기
            val inputStream = body.byteStream()
            val outputStream = FileOutputStream(file)
            val buffer = ByteArray(4096)
            var bytesRead: Int

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()

            true // 파일 저장 성공
        } catch (e: IOException) {
            e.printStackTrace()
            false // 파일 저장 실패
        }
    }

    companion object{
        const val TAG = "BoardActivity"
    }
}