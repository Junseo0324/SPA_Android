package com.example.spa_android.Adapter

import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spa_android.KoreanUtils
import com.example.spa_android.MessgeActivity
import com.example.spa_android.R
import com.example.spa_android.data.RoomListDTO
import com.example.spa_android.databinding.ChatRecyclerBinding
import com.example.spa_android.retrofit.RetrofitApplication

class ChatListAdapter(
    private val itemList: ArrayList<RoomListDTO>,
    private val onItemClick: (String,String) -> Unit
): RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>(){
    private var filteredList: ArrayList<RoomListDTO> = itemList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListAdapter.ChatListViewHolder {
        val binding = ChatRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ChatListViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ChatListAdapter.ChatListViewHolder, position: Int) {


        holder.name.text = filteredList[position].partnerName
        holder.chatName.text = filteredList[position].chatName
        holder.lastChat.text = filteredList[position].lastMessage
        holder.chatTime.text = filteredList[position].timestamp

        //unReadCount 가 0일 때는 알림 표시 제외
        val unreadCount = filteredList[position].unReadCount
        if (unreadCount > 0) {
            holder.notificationNum.text = unreadCount.toString()
            holder.notificationNum.visibility = View.VISIBLE // 알림 수 표시
            holder.icon.visibility = View.VISIBLE // 알림 아이콘 표시
        } else {
            holder.notificationNum.visibility = View.GONE // 숨김
            holder.icon.visibility = View.GONE // 숨김
        }

        Glide.with(holder.itemView.context)
            .load(RetrofitApplication.BASE_URL+filteredList[position].filepath)
            .placeholder(R.drawable.sample_user) // 로딩 중에 보여줄 이미지
            .into(holder.image)


        holder.linear.setOnClickListener {
            val intent = Intent(holder.itemView.context, MessgeActivity::class.java)
            intent.putExtra("chatName",filteredList[position].chatName)
            intent.putExtra("roomId",filteredList[position].roomId)
            intent.putExtra("myEmail",filteredList[position].myEmail)
            intent.putExtra("myName",filteredList[position].myName)
            intent.putExtra("projectNumber",filteredList[position].projectId)
            Log.d("MessageActivity", "onBindViewHolder: projectNumber ${filteredList[position].projectId}")
            intent.putExtra("partner",filteredList[position].partnerName) //상대방
            intent.putExtra("partnerEmail",filteredList[position].partner) //상대방 이메일
            onItemClick(filteredList[position].roomId,filteredList[position].myEmail)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun filter(query: String?) {
        filteredList = if (query.isNullOrEmpty()) {
            itemList
        } else {
            val lowerCaseQuery = query.lowercase()
            val initialSoundQuery = KoreanUtils.getKoreanInitialSound(lowerCaseQuery) // 초성 검색을 위한 변환

            itemList.filter {
                it.partnerName.contains(lowerCaseQuery, ignoreCase = true) || // 기본 검색
                        containsText(it.partnerName, lowerCaseQuery) || // 기존 문자 검색
                        KoreanUtils.getKoreanInitialSound(it.partnerName).startsWith(initialSoundQuery) // 초성 검색
            } as ArrayList<RoomListDTO>
        }
        notifyDataSetChanged() // 데이터가 변경되었음을 알림
    }

    // 기존 containsText 메서드
    private fun containsText(name: String, query: String): Boolean {
        for (char in query) {
            if (name.contains(char, ignoreCase = true)) {
                return true
            }
        }
        return false
    }



    inner class ChatListViewHolder(private val binding: ChatRecyclerBinding):RecyclerView.ViewHolder(binding.root){
        val image = binding.chatListImage
        val name = binding.chatuser
        val chatName = binding.chatName
        val lastChat = binding.lastChat
        val chatTime = binding.lastChatTime
        val notificationNum = binding.chatNotiNum
        val icon = binding.NotificationIcon
        val linear = binding.chatLinear
    }

    companion object{
        const val TAG = "ChatListAdapter"
    }
}