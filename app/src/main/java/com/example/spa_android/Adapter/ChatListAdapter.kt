package com.example.spa_android.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spa_android.MessgeActivity
import com.example.spa_android.data.ChatList
import com.example.spa_android.databinding.ChatRecyclerBinding

class ChatListAdapter(private val itemList: ArrayList<ChatList>):
RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>(){
    private var filteredList: ArrayList<ChatList> = itemList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListAdapter.ChatListViewHolder {
        val binding = ChatRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ChatListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatListAdapter.ChatListViewHolder, position: Int) {
        holder.name.text = filteredList[position].name
        holder.lastChat.text = filteredList[position].chat
        holder.chatTime.text = filteredList[position].time
        holder.notificationNum.text = filteredList[position].notification

        holder.linear.setOnClickListener {
            val intent = Intent(holder.itemView.context,MessgeActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    // 필터링 메서드 추가
    fun filter(query: String?) {
        println("Filtering with query: $query")
        filteredList = if (query.isNullOrEmpty()) {
            itemList
        } else {
            itemList.filter {
                it.name.contains(query, ignoreCase = true) ||
                containsText(it.name,query)
            } as ArrayList<ChatList>
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
        val name = binding.chatuser
        val lastChat = binding.lastChat
        val chatTime = binding.lastChatTime
        val notificationNum = binding.chatNotiNum
        val linear = binding.chatLinear
    }


}