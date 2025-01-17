package com.example.spa_android

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spa_android.Adapter.ChatingItemAdapter
import com.example.spa_android.data.ChatRoomDTO
import com.example.spa_android.data.FCMDTO
import com.example.spa_android.data.FCMDataDTO
import com.example.spa_android.data.MessageDTO
import com.example.spa_android.databinding.ActivityMessgeBinding
import com.example.spa_android.retrofit.RetrofitApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessgeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessgeBinding
    private lateinit var adapter: ChatingItemAdapter
    private lateinit var sharedPreferences : SharedPreferences
    private var chattingItemList = ArrayList<MessageDTO>()
    private var myEmail = "None"
    private var chatName = "None"
    private var roomId = "0"
    private var chatUser = "UnknownUser"
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable) //핸들러 정리
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        roomId = intent.getStringExtra("roomId").toString()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessgeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyInformation", Context.MODE_PRIVATE)
        myEmail = sharedPreferences.getString("email","NonUser").toString()
        chatName = intent.getStringExtra("chatName").toString()
        roomId = intent.getStringExtra("roomId").toString()
        var partner = intent.getStringExtra("partner").toString()
        binding.messageToolbar.title = partner
        binding.messageToolbar.subtitle = chatName
        Log.d(TAG, "onCreate: roomId : $roomId and chatName : $chatName")

        chatUser = intent.getStringExtra("partnerEmail").toString() //상대는 받는 사람

        Log.d(TAG, "onCreate: chatName $chatName and chatUser $chatUser")

        adapter = ChatingItemAdapter(this,chattingItemList,myEmail)
        binding.chatingRecycler.adapter = adapter
        binding.chatingRecycler.layoutManager = LinearLayoutManager(this)

        fetchChattingItems(roomId)

        setSupportActionBar(binding.messageToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back)

        adapter.notifyDataSetChanged()

        startPollingForMessages()
        binding.messageBtn.setOnClickListener {
            sendMessage()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                startActivity(Intent(this,MainActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun fetchChattingItems(roomId: String) {
        RetrofitApplication.networkService.getRoomMessage(roomId).enqueue(object : Callback<List<MessageDTO>> {
            override fun onResponse(call: Call<List<MessageDTO>>, response: Response<List<MessageDTO>>) {
                if (response.isSuccessful) {
                    val chattingItems = response.body()
                    if (chattingItems != null) {
                        // 기존 데이터를 유지하고 새로운 데이터 추가
                        chattingItemList.clear()
                        chattingItemList.addAll(chattingItems) // 새로운 데이터 추가
                        adapter.notifyDataSetChanged() // RecyclerView에 데이터 변경 알림
                        binding.chatingRecycler.scrollToPosition(chattingItemList.size - 1) // 최신 메시지로 스크롤
                    } else {
                        Log.d(TAG, "Response body is null")
                    }
                } else {
                    Log.d(TAG, "Error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<MessageDTO>>, t: Throwable) {
                Log.d(TAG, "Failed to fetch chatting items: $t")
            }
        })
    }

    private fun sendMessage(){
        var message = binding.insertMessageEdit.text.toString()
        if(message.isEmpty()){
            return
        }

        var projectId = intent.getStringExtra("projectNumber")
        Log.d(TAG, "onCreate: projectId: $projectId")

        val newMessage = MessageDTO(sender = myEmail, receiver = chatUser, senderName = intent.getStringExtra("myName").toString(), receiverName = intent.getStringExtra("partner").toString(), message = message, isRead = false)
        val name = sharedPreferences.getString("name",null).toString()
        Log.d(TAG, "sendMessage: name $name")

        chattingItemList.add(newMessage) // 로컬 데이터 리스트에 추가
        adapter.notifyItemInserted(chattingItemList.size - 1) // RecyclerView에 새 메시지 추가 알림
        binding.chatingRecycler.scrollToPosition(chattingItemList.size - 1) // 최신 메시지로 스크롤


        val fcmDataDTO = FCMDataDTO(
            message = FCMDataDTO.Message(
                token = null,
                data = FCMDataDTO.Data(
                    id = null,
                    sender = myEmail,
                    receiver = chatUser,
                    chatName = chatName,
                    timestamp = System.currentTimeMillis().toString(),
                    teamId = projectId.toString(),
                    roomId = roomId,
                    partnerName = intent.getStringExtra("myName").toString()
                )
            )
        )

        val fcmDTO = FCMDTO(
            message = FCMDTO.Message(
                token = null, // 서버가 receiver를 기반으로 토큰 검색
                notification = FCMDTO.Notification(
                    title = name,
                    body = message
                ),
                data = FCMDTO.Data(
                    id = null,
                    sender = myEmail,
                    receiver = chatUser,
                    chatName = chatName,
                    timestamp = System.currentTimeMillis().toString(),
                    teamId = projectId.toString(),
                    roomId = roomId,
                    partnerName = intent.getStringExtra("myName").toString()
                )
            )
        )
        Log.d(TAG, "sendMessage: fcmDTO : $fcmDTO and fcmDataDTO: $fcmDataDTO")
        RetrofitApplication.networkService.sendNotificationData(fcmDTO).clone()?.enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Log.d(TAG, "onResponse: sendNotification $fcmDTO")
                }
                else {
                    Log.d(TAG, "onResponse: ${response.errorBody()?.toString()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }


        })

        RetrofitApplication.networkService.sendData(fcmDataDTO).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Message sent and notification requested")
                } else {
                    Log.e(TAG, "Failed to send message: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e(TAG, "Error sending message: ${t.message}")
            }
        })

        var sendMessage = ChatRoomDTO(sender = myEmail, receiver = chatUser,message = message, projectId = projectId.toString())

        RetrofitApplication.networkService.sendMessageToUser(sendMessage).clone()?.enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                updateRecyclerView()
                Log.d(TAG, "onResponse: ${response.body()}")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })

        binding.insertMessageEdit.text.clear()
    }


    private fun updateRecyclerView(){
        //데이터 업데이트
        adapter.notifyDataSetChanged()
    }

    private fun startPollingForMessages() {
        handler = Handler(Looper.getMainLooper()) //메인 스레드에서 실행
        runnable = object : Runnable {
            override fun run() {
                fetchChattingItems(roomId) // 새로운 메시지 가져오기
                handler.postDelayed(this, 1000) // 1초마다 반복
            }
        }
        handler.post(runnable)
    }

    companion object{
        const val TAG = "MessageActivity"
    }
}