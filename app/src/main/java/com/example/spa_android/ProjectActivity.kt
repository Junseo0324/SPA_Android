package com.example.spa_android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spa_android.Adapter.FileItemAdapter
import com.example.spa_android.Adapter.InformationItemAdapter
import com.example.spa_android.Adapter.MemberItemAdapter
import com.example.spa_android.data.FileItem
import com.example.spa_android.data.InformationItem
import com.example.spa_android.data.MemberItem
import com.example.spa_android.databinding.ActivityProjectBinding

class ProjectActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProjectBinding
    private lateinit var memberAdapter : MemberItemAdapter
    private lateinit var informationAdapter : InformationItemAdapter
    private lateinit var fileAdapter : FileItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //프로젝트 이름 설정
        binding.ProjectName.text = intent.getStringExtra("projectName")

        val memberItemList = arrayListOf(
            MemberItem("허준서","Android","현재 활동 중"),
            MemberItem("박성호","Backend","운동 중"),
            MemberItem("임채률","Android","쉬는 중"),
            MemberItem("임원섭","Backend","현재 활동 중")
        )

        val informationItemList = arrayListOf(
            InformationItem("공지사항 1","내용"),
            InformationItem("공지사항 2","내용 2"),
            InformationItem("공지사항 3","내용 3")
        )

        val fileItemList = arrayListOf(
            FileItem("파일 1","Intent","20:02"),
            FileItem("파일 2","Retrofit","20:10"),
            FileItem("파일 3","Firebase","20:12")
        )
        //Adapter 설정
        memberAdapter = MemberItemAdapter(memberItemList)
        informationAdapter = InformationItemAdapter(informationItemList)
        fileAdapter = FileItemAdapter(fileItemList)

        binding.memberRecycler.adapter = memberAdapter
        if(memberItemList.size<=2){
            binding.memberRecycler.layoutManager = LinearLayoutManager(this)
        }
        else{
            binding.memberRecycler.layoutManager = GridLayoutManager(this,2)
        }
        binding.informationRecycler.adapter = informationAdapter
        binding.fileRecycler.adapter = fileAdapter

        binding.informationRecycler.layoutManager = LinearLayoutManager(this)
        binding.fileRecycler.layoutManager = LinearLayoutManager(this)

        //-------- Adapter 설정 End------//

        //Intent//
        binding.infoBtn.setOnClickListener {
            val intent = Intent(this,InsertInfoActivity::class.java)
            startActivity(intent)
        }
        binding.fileButton.setOnClickListener {
            val intent = Intent(this,ProjectFileActivity::class.java)
            startActivity(intent)
        }


    }
}