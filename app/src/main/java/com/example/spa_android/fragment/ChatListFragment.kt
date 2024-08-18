package com.example.spa_android.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spa_android.Adapter.ChatListAdapter
import com.example.spa_android.data.ChatList
import com.example.spa_android.databinding.FragmentChatListBinding

class ChatListFragment : Fragment() {
    private lateinit var binding: FragmentChatListBinding
    private lateinit var chatListAdapter : ChatListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChatListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chatListItemArray = arrayListOf(
            ChatList("박성호","헤응","17:30","1"),
            ChatList("임채률","ㅋㅋ","13:30","2"),
            ChatList("홍길동","슉","19:30","5"),
            ChatList("임원섭","똥","14:42","3"),
            ChatList("정대로","수현 사랑해","14:22","4"),
            ChatList("이수현","대로 보고싶어","03:23","1"),
            ChatList("고재진","퇴사하고 싶다","15:22","2")
        )
        chatListAdapter = ChatListAdapter(chatListItemArray)
        binding.chatRv.adapter = chatListAdapter
        binding.chatRv.layoutManager = LinearLayoutManager(context)

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
}