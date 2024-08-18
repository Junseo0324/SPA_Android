package com.example.spa_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spa_android.Adapter.ChatingItemAdapter
import com.example.spa_android.data.ChatingItem
import com.example.spa_android.databinding.ActivityMessgeBinding

class MessgeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessgeBinding
    private lateinit var adapter: ChatingItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMessgeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val chatingItemList = arrayListOf(
            ChatingItem("박성호","hi","0"),
            ChatingItem("임채률","nice","1"),
            ChatingItem("박성호","좋아","0"),
            ChatingItem("임채률","ㅋㅋ","1"),
            ChatingItem("박성호","채률아 너는 정말 착하고 착하고 나쁜거 같아 너가 세상에서 제일 나빠","0"),
            ChatingItem("임채률","아니","1"),
            ChatingItem("박성호","마스터 갈래","0")
        )

        adapter = ChatingItemAdapter(chatingItemList)

        binding.chatingRecycler.adapter = adapter
        binding.chatingRecycler.layoutManager = LinearLayoutManager(this)

    }
}