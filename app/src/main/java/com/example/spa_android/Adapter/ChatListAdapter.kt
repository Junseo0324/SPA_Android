package com.example.spa_android.Adapter

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spa_android.MessgeActivity
import com.example.spa_android.R
import com.example.spa_android.databinding.ChatRecyclerBinding
import com.example.spa_android.retrofit.ChatSummaryDTO
import com.example.spa_android.retrofit.RetrofitApplication
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatListAdapter(
    private val itemList: ArrayList<ChatSummaryDTO>,
    private val onItemClick: (String,String) -> Unit
): RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>(){
    private var filteredList: ArrayList<ChatSummaryDTO> = itemList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListAdapter.ChatListViewHolder {
        val binding = ChatRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ChatListViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ChatListAdapter.ChatListViewHolder, position: Int) {


        holder.name.text = filteredList[position].senderName
        holder.lastChat.text = filteredList[position].latestMessage
        holder.chatTime.text = formatTimestamp(filteredList[position].timestamp)

        //unReadCount 가 0일 때는 알림 표시 제외
        val unreadCount = filteredList[position].unreadCount
        if (unreadCount > 0) {
            holder.notificationNum.text = unreadCount.toString()
            holder.notificationNum.visibility = View.VISIBLE // 알림 수 표시
            holder.icon.visibility = View.VISIBLE // 알림 아이콘 표시
        } else {
            holder.notificationNum.visibility = View.GONE // 숨김
            holder.icon.visibility = View.GONE // 숨김
        }

        Glide.with(holder.itemView.context)
            .load(RetrofitApplication.BASE_URL+filteredList[position].filePath)
            .placeholder(R.drawable.sample_user) // 로딩 중에 보여줄 이미지
            .into(holder.image)

        holder.linear.setOnClickListener {
            val intent = Intent(holder.itemView.context,MessgeActivity::class.java)
            intent.putExtra("chatName",filteredList[position].chatName)
            intent.putExtra("sender",filteredList[position].sender)
            onItemClick(filteredList[position].sender,filteredList[position].receiver)
            intent.putExtra("receiver",filteredList[position].receiver)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    // SearchView filtering itemList
    fun filter(query: String?) {
        filteredList = if (query.isNullOrEmpty()) {
            itemList
        } else {
            itemList.filter {
                it.sender.contains(query, ignoreCase = true) ||
                containsText(it.sender,query)
            } as ArrayList<ChatSummaryDTO>
        }
        println("Filtered List: $filteredList")
        notifyDataSetChanged() // 데이터가 변경되었음을 알림
    }

    private fun containsText(name: String, query: String): Boolean{
        for(char in query) {
            if (name.contains(char,ignoreCase = true)){
                return true
            }
        }
        return false
    }

    inner class ChatListViewHolder(private val binding: ChatRecyclerBinding):RecyclerView.ViewHolder(binding.root){
        val image = binding.chatListImage
        val name = binding.chatuser
        val lastChat = binding.lastChat
        val chatTime = binding.lastChatTime
        val notificationNum = binding.chatNotiNum
        val icon = binding.NotificationIcon
        val linear = binding.chatLinear
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatTimestamp(timestamp: String): String{
        val dateTime = LocalDateTime.parse(timestamp)
        val formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")

        return dateTime.format(formatter)
    }

    companion object{
        const val TAG = "ChatListAdapter"
    }
}