package com.example.spa_android

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spa_android.Adapter.ChatingItemAdapter
import com.example.spa_android.data.ChatingItem
import com.example.spa_android.data.FCMDTO
import com.example.spa_android.data.FCMDataDTO
import com.example.spa_android.databinding.ActivityMessgeBinding
import com.example.spa_android.retrofit.ChatModel
import com.example.spa_android.retrofit.ChatRequestModel
import com.example.spa_android.retrofit.RetrofitApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessgeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessgeBinding
    private lateinit var adapter: ChatingItemAdapter
    private lateinit var sharedPreferences : SharedPreferences
    private var chatingItemList = ArrayList<ChatingItem>()
    private var chatModelItem = ArrayList<ChatModel>()
    private lateinit var chatRequestModel: ChatRequestModel
    private var myEmail = "None"
    private var chatName = "test"
    private var chatUser: String? = null
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable) //핸들러 정리
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessgeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyInformation", Context.MODE_PRIVATE)
        myEmail = sharedPreferences.getString("email","NonUser").toString()
        chatName = intent.getStringExtra("chatName").toString()

        var partner = intent.getStringExtra("partner").toString()
        var partnerEmail = intent.getStringExtra("partnerEmail").toString()

        Log.d(TAG, "onCreate: partner : $partner")
        Log.d(TAG, "onCreate: partnerEmail : $partnerEmail")
        if(myEmail.equals(intent.getStringExtra("sender").toString())){ //보낸 사람이 나면
            chatUser = intent.getStringExtra("receiver").toString() //상대는 받는 사람
        }else{
            chatUser = intent.getStringExtra("sender").toString() // 아니면 상대가 보낸 사람
        }
        Log.d(TAG, "onCreate: chatName $chatName")

        adapter = ChatingItemAdapter(this,chatingItemList)
        binding.chatingRecycler.adapter = adapter
        binding.chatingRecycler.layoutManager = LinearLayoutManager(this)

        fetchChattingItems(chatName,myEmail)

        adapter.notifyDataSetChanged()

        startPollingForMessages()
        binding.messageBtn.setOnClickListener {
            sendMessage()
        }

    }
    private fun fetchChattingItems(chatName: String, sharedEmail: String) {
        RetrofitApplication.networkService.getChattingItems(chatName, sharedEmail).enqueue(object : Callback<List<ChatingItem>> {
            override fun onResponse(call: Call<List<ChatingItem>>, response: Response<List<ChatingItem>>) {
                if (response.isSuccessful) {
                    val chattingItems = response.body()
                    if (chattingItems != null) {
                        // 기존 데이터를 유지하고 새로운 데이터 추가
                        chatingItemList.clear()
                        chatingItemList.addAll(chattingItems) // 새로운 데이터 추가
                        adapter.notifyDataSetChanged() // RecyclerView에 데이터 변경 알림
                        binding.chatingRecycler.scrollToPosition(chatingItemList.size - 1) // 최신 메시지로 스크롤
                    } else {
                        Log.d(TAG, "Response body is null")
                    }
                } else {
                    Log.d(TAG, "Error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ChatingItem>>, t: Throwable) {
                Log.d(TAG, "Failed to fetch chatting items: $t")
            }
        })
    }

    private fun sendMessage(){
        val data = getRequestData()
        var message = binding.insertMessageEdit.text.toString()
        if(message.isEmpty()){
            return
        }
        // 새로운 메시지를 로컬 리스트에 추가 (즉시 반영)
        val state = 0
        val newMessage = ChatingItem(name = myEmail, message = message, state = state, chatName = chatName)
        val name = sharedPreferences.getString("name",null).toString()

        chatingItemList.add(newMessage) // 로컬 데이터 리스트에 추가
        adapter.notifyItemInserted(chatingItemList.size - 1) // RecyclerView에 새 메시지 추가 알림
        binding.chatingRecycler.scrollToPosition(chatingItemList.size - 1) // 최신 메시지로 스크롤


        val fcmDataDTO = FCMDataDTO(
            message = FCMDataDTO.Message(
                token = null,
                data = FCMDataDTO.Data(
                    id = null,
                    sender = myEmail,
                    receiver = chatUser.toString(),
                    chatName = chatName,
                    timestamp = System.currentTimeMillis().toString(),
                    teamId = data.second
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
                    receiver = chatUser.toString(),
                    chatName = chatName,
                    timestamp = System.currentTimeMillis().toString(),
                    teamId = data.second
                )
            )
        )

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

        chatRequestModel = ChatRequestModel(myEmail,chatUser.toString(),message,chatName,data.second)
        RetrofitApplication.networkService.sendMessage(chatRequestModel).clone()?.enqueue(object : Callback<ChatRequestModel>{
            override fun onResponse(call: Call<ChatRequestModel>, response: Response<ChatRequestModel>) {
                Log.d(TAG, "onResponse: ${response.body()}")
                //chatModelItem에 업데이트하는 메서드 필요?
                updateRecyclerView()
            }
            override fun onFailure(call: Call<ChatRequestModel>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }

        })

        binding.insertMessageEdit.text.clear()
    }

    private fun getRequestData(): Pair<String,String>{
        var receiver = myEmail
        var teamId = "SPA"
        for(chatModel in chatModelItem){
            if( chatModel.sender == myEmail){
                receiver = chatModel.receiver
                teamId  = chatModel.teamId
            }
        }
        return Pair(receiver,teamId)
    }

    private fun updateRecyclerView(){
        //데이터 업데이트
        adapter.notifyDataSetChanged()
    }

    private fun startPollingForMessages() {
        handler = Handler(Looper.getMainLooper()) //메인 스레드에서 실행
        runnable = object : Runnable {
            override fun run() {
                fetchChattingItems(chatName, myEmail) // 새로운 메시지 가져오기
                handler.postDelayed(this, 5000) // 5초마다 반복
            }
        }
        handler.post(runnable)
    }

    companion object{
        const val TAG = "MessageActivity"
    }
}