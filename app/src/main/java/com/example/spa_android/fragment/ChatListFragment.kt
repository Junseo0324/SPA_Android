package com.example.spa_android.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spa_android.Adapter.ChatListAdapter
import com.example.spa_android.data.RoomListDTO
import com.example.spa_android.databinding.FragmentChatListBinding
import com.example.spa_android.retrofit.RetrofitApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatListFragment : Fragment() {
    private lateinit var binding: FragmentChatListBinding
    private lateinit var chatListAdapter : ChatListAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    var roomList = ArrayList<RoomListDTO>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = requireActivity().getSharedPreferences("MyInformation",Context.MODE_PRIVATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 핸들러 정리
        handler.removeCallbacks(runnable) 
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChatListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatListAdapter = ChatListAdapter(roomList){
            roomID, email -> markMessageAsRead(roomID,email)
        }
        binding.chatRv.adapter = chatListAdapter
        binding.chatRv.layoutManager = LinearLayoutManager(context)
        chatListAdapter.notifyDataSetChanged()

        val email = sharedPreferences.getString("email",null)
        if(email!=null) {
            getChatListFromEmail(email)
        }

        startPollingForMessages()

        //SearchView
        binding.chatSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("SearchView","Searching for: $newText")
                chatListAdapter.filter(newText)
                return true
            }
        })
    }

    //내가 포함된 채팅목록
    private fun getChatListFromEmail(email: String){
        RetrofitApplication.networkService.getRoomByEmail(email).clone()?.enqueue(object : Callback<ArrayList<RoomListDTO>>{
            override fun onResponse(call: Call<ArrayList<RoomListDTO>>, response: Response<ArrayList<RoomListDTO>>) {
                roomList.clear()
                roomList.addAll(response.body() ?: emptyList())
                Log.d(TAG, "onResponse: roomList --  $roomList")
                chatListAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ArrayList<RoomListDTO>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun markMessageAsRead(roomId: String, email: String) {
        RetrofitApplication.networkService.markMessageAsRead(roomId, email).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Messages marked as read roomId: $roomId and email: $email")

                } else {
                    Log.d(TAG, "Failed to mark messages as read: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d(TAG, "Network call failed: $t")
            }
        })
    }


    private fun startPollingForMessages() {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                val userInformation = sharedPreferences.getString("email", null)
                if (userInformation != null) {
                    getChatListFromEmail(userInformation) // 주기적으로 채팅 리스트 가져오기
                }
                handler.postDelayed(this, 3000) // 1초마다 반복
            }
        }
        handler.post(runnable)
    }
    companion object{
        const val TAG = "ChatListFragment"
    }
}