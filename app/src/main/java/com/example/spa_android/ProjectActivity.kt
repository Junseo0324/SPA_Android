package com.example.spa_android

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spa_android.Adapter.FileItemAdapter
import com.example.spa_android.Adapter.InformationItemAdapter
import com.example.spa_android.Adapter.MemberItemAdapter
import com.example.spa_android.data.ChatRoomDTO
import com.example.spa_android.data.RoomListDTO
import com.example.spa_android.databinding.ActivityProjectBinding
import com.example.spa_android.databinding.ConditionDialogBinding
import com.example.spa_android.databinding.MessageDialogBinding
import com.example.spa_android.retrofit.MemberDTO
import com.example.spa_android.retrofit.ProjectContentEntity
import com.example.spa_android.retrofit.RetrofitApplication
import com.example.spa_android.retrofit.TeamProjectDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ProjectActivity : AppCompatActivity(), OnMemberStateChangeListener, OnProjectFileListener, OnProjectInfomationListener {
    private lateinit var binding : ActivityProjectBinding
    private lateinit var memberAdapter : MemberItemAdapter
    private lateinit var informationAdapter : InformationItemAdapter
    private lateinit var fileAdapter : FileItemAdapter
    private lateinit var projectDTO : TeamProjectDTO
    private var informationList : ArrayList<ProjectContentEntity> = ArrayList()
    private var fileList : ArrayList<ProjectContentEntity> = ArrayList()
    private lateinit var projectId : String
    private lateinit var sharedPreferences : SharedPreferences
    private var memberDTO : ArrayList<MemberDTO> = ArrayList()
    private var roomListDTO: RoomListDTO? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("MyInformation",Context.MODE_PRIVATE)
        var email = sharedPreferences.getString("email",null)
        binding = ActivityProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 표시
        supportActionBar?.setDisplayShowHomeEnabled(true)


        projectId = intent.getStringExtra("selectedProject").toString()
        Log.d(TAG, "onCreate: selectProject ${intent.getStringExtra("selectedProject")}")
        getListByProjectId(intent.getStringExtra("selectedProject").toString())
        getInformationList(projectId)
        //프로젝트 이름 설정
        binding.toolbar.title = intent.getStringExtra("selectedProjectName")

        //Member Adapter
        memberAdapter = MemberItemAdapter(memberDTO,this)
        binding.memberRecycler.adapter = memberAdapter
        binding.memberRecycler.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        //Information Adapter
        informationAdapter = InformationItemAdapter(informationList,this,email.toString())
        binding.informationRecycler.layoutManager = LinearLayoutManager(this)
        binding.informationRecycler.adapter = informationAdapter


        //File Adapter
        fileAdapter = FileItemAdapter(fileList,this,email.toString())
        binding.fileRecycler.layoutManager = LinearLayoutManager(this)
        binding.fileRecycler.adapter = fileAdapter


        binding.infoBtn.setOnClickListener {
            val intent = Intent(this,InsertInfoActivity::class.java)
            intent.putExtra("projectId",projectId)
            intent.putExtra("isEdit",false) //작성버튼
            startActivity(intent)
        }

        binding.fileButton.setOnClickListener {
            val intent = Intent(this,ProjectFileActivity::class.java)
            intent.putExtra("projectId",projectId)
            startActivity(intent)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // 뒤로가기 버튼 클릭 시 액티비티 종료
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getListByProjectId(id: String){
        RetrofitApplication.networkService.getProjectListById(id).clone()?.enqueue(object : Callback<TeamProjectDTO>{
            override fun onResponse(call: Call<TeamProjectDTO>, response: Response<TeamProjectDTO>) {
                if(response.isSuccessful){
                    val teamProject = response.body()
                    teamProject?.let {
                        projectDTO = it // projectDTO에 직접 할당
                        memberDTO.clear()
                        memberDTO.addAll(it.members ?: emptyList())
                        memberAdapter.notifyDataSetChanged()

                        Log.d(TAG, "onResponse:  memberDTO: $memberDTO")
                        Log.d(TAG, "onResponse:  projectDTO: $projectDTO")
                    }
                }
            }

            override fun onFailure(call: Call<TeamProjectDTO>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    override fun changeState(item: MemberDTO) {
        var email = sharedPreferences.getString("email",null)
        if (email.equals(item.email)){
            showConditionDialog(item)
        }
    }

    override fun newChat(item: MemberDTO) {
        showNewChatMessage(item)
    }



    private fun createChat(item: MemberDTO,message: String){
        val email = sharedPreferences.getString("email",null)


        val chatRoomDTO = ChatRoomDTO(
            email.toString(),item.email,message,projectId
        )



        RetrofitApplication.networkService.sendMessageToUser(chatRoomDTO).clone()?.enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    getChatListFromEmail(email.toString(),item.email,projectId)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun getChatListFromEmail(email: String,receiver:String,projectId: String) {
        RetrofitApplication.networkService.getChatListData(email,receiver,projectId).clone()?.enqueue(object :Callback<RoomListDTO>{
            override fun onResponse(call: Call<RoomListDTO>, response: Response<RoomListDTO>) {
                if(response.isSuccessful){
                    roomListDTO = response.body()
                    roomListDTO?.let {
                        startMessageActivity(it)
                    } ?: run {
                        intent = Intent(this@ProjectActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }
            }

            override fun onFailure(call: Call<RoomListDTO>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun startMessageActivity(roomListDTO: RoomListDTO) {
        val intent = Intent(this@ProjectActivity, MessgeActivity::class.java)
        intent.putExtra("chatName", roomListDTO.chatName)
        intent.putExtra("roomId", roomListDTO.roomId)
        intent.putExtra("myEmail", roomListDTO.myEmail)
        intent.putExtra("projectNumber", roomListDTO.projectId)
        intent.putExtra("partner", roomListDTO.partnerName) //상대방
        intent.putExtra("partnerEmail", roomListDTO.partner) //상대방 이메일
        startActivity(intent)
    }


    private fun showNewChatMessage(item: MemberDTO) {
        val dialogBinding = MessageDialogBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogBinding.root)

        builder.setPositiveButton("전송") { dialog, _ ->
            val message = dialogBinding.dialogInput.text.toString()
            if (message.isNotBlank()) {
                createChat(item, message) // 입력된 메시지를 전달
            } else {
                Toast.makeText(this, "메시지를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun sendToState(item: MemberDTO){
        val data = getStatusData(item)
        Log.d(TAG, "sendToState: $data")
        Log.d(TAG, "sendToState: ${item.email}")
        RetrofitApplication.networkService.changeConditionsMember(item.email,data).clone()?.enqueue(object : Callback<Map<String,String>>{
            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if(response.isSuccessful){
                    Log.d(TAG, "onResponse: ${response.message()}")
                    getListByProjectId(intent.getStringExtra("selectedProject").toString())
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }


    private fun getStatusData(item: MemberDTO) : Map<String,String> {
        val data= mutableMapOf<String,String>()
        data["conditions"] = item.conditions
        data["projectId"] = intent.getStringExtra("selectedProject").toString()

        return data
    }


    private fun showConditionDialog(item: MemberDTO) {
        val conditionBinding = ConditionDialogBinding.inflate(layoutInflater)
        val conditions = arrayOf("활동 중", "자리 비움", "오프라인", "휴가")
        val builder = AlertDialog.Builder(this)
        builder.setView(conditionBinding.root)
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,conditions)
        conditionBinding.conditionsList.adapter = adapter

        val dialog = builder.create()
        conditionBinding.conditionsList.setOnItemClickListener { parent, view, position, id ->
            item.conditions = conditions[position]
            sendToState(item)
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun getInformationList(id: String){
        RetrofitApplication.networkService.getProjectInformation(id).clone()?.enqueue(object: Callback<ArrayList<ProjectContentEntity>>{
            override fun onResponse(call: Call<ArrayList<ProjectContentEntity>>, response: Response<ArrayList<ProjectContentEntity>>) {
                if(response.isSuccessful){
                    Log.d(TAG, "onResponse: projectId $id")
                    Log.d(TAG, "onResponse: responseBody ${response.body()}")
                    val body = response.body()
                    if (body != null) {
                        informationList.clear()
                        informationList.addAll(body)
                        informationAdapter.notifyDataSetChanged()
                        Log.d(TAG, "onResponse: $informationList")
                    } else {
                        Log.e(TAG, "Response body is null")
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<ProjectContentEntity>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }


    private fun getFileList(id:String) {
        RetrofitApplication.networkService.getFileList(id).clone()?.enqueue(object : Callback<ArrayList<ProjectContentEntity>>{
            override fun onResponse(call: Call<ArrayList<ProjectContentEntity>>, response: Response<ArrayList<ProjectContentEntity>>) {
                if (response.isSuccessful){
                    val body = response.body()
                    if (body !=null) {
                        fileList.clear()
                        fileList.addAll(response.body()!!)
                        fileAdapter.notifyDataSetChanged()
                        Log.d(TAG, "onResponse: $fileList")
                    }else{
                        Log.e(TAG, "Response body is null")

                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<ProjectContentEntity>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }


    override fun downloadFile(id: String) {
        RetrofitApplication.networkService.downloadFile(id).clone()?.enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->

                        // MIME 타입 확인
                        val mimeType = response.headers()["Content-Type"]
                        val extension = getFileExtensionFromMimeType(mimeType)

                        val fileName = "SPA_${sharedPreferences.getString("email", "default")}_${id}.$extension"
                        val success = saveFileToDisk(body, fileName)
                        if (success) {
                            Log.d(TAG, "File downloaded successfully")
                            Toast.makeText(this@ProjectActivity, "파일 다운로드 성공", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e(TAG, "Failed to save file")
                            Toast.makeText(this@ProjectActivity, "파일 저장 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e(TAG, "Download failed with response code: ${response.code()}")
                    Toast.makeText(this@ProjectActivity, "파일 다운로드 실패", Toast.LENGTH_SHORT).show()
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


    override fun onResume() {
        super.onResume()
        getInformationList(projectId)
        getFileList(projectId)
    }



    companion object{
        const val TAG="ProjectActivity"
    }

    override fun deleteItem(item: ProjectContentEntity) {
        RetrofitApplication.networkService.deleteContent(item.id).clone()?.enqueue(object :Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    DialogUtils.showApplyDialog(this@ProjectActivity,"공지사항 삭제","공지사항 삭제가 완료되었습니다."){
                        getInformationList(projectId)
                    }
                    Log.d(TAG, "onResponse: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }


    override fun deleteFile(item: ProjectContentEntity) {
        RetrofitApplication.networkService.deleteContent(item.id).clone()?.enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    DialogUtils.showApplyDialog(this@ProjectActivity,"파일 삭제","파일 삭제가 완료되었습니다."){
                        getFileList(projectId)
                    }
                    Log.d(TAG, "onResponse: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

}