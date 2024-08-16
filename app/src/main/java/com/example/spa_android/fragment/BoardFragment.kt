package com.example.spa_android.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

import com.example.spa_android.WriteBoradActivity

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spa_android.Adapter.BoardItemAdapter
import com.example.spa_android.EnrollMemberActivity
import com.example.spa_android.data.BoardItem
import com.example.spa_android.databinding.FragmentBoardBinding


class BoardFragment : Fragment() {
    private lateinit var binding: FragmentBoardBinding
    private lateinit var boardAdapter: BoardItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    private val registerActivity: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult() // ◀ StartActivityForResult 처리를 담당
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val value = result.data?.getStringExtra("resultData")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBoardBinding.inflate(inflater,container,false)
        return binding.root

    }        
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val boardItemList = arrayListOf(
            BoardItem("허준서", "프로젝트1", "내용 미리보기1", "18:40"),
            BoardItem("임채률", "프로젝트2", "내용 미리보기2", "18:44"),
            BoardItem("박성호", "프로젝트3", "내용 미리보기3", "18:47"),
            BoardItem("임원섭", "프로젝트4", "내용 미리보기4", "18:48")
        )
        // 어댑터 생성 및 설정
        boardAdapter = BoardItemAdapter(boardItemList)
        binding.boardRecycler.adapter = boardAdapter
        binding.boardRecycler.layoutManager = LinearLayoutManager(requireContext())


        /*binding.submitBtn.setOnClickListener {
            // enroll_member_activity로 전송하는 코드
            val intent = Intent(context, EnrollMemberActivity::class.java)
            registerActivity.launch(intent) // 변경해야됌 일단 걸어논거 */

        // 버튼 클릭 리스너 설정
        binding.writeBtn.setOnClickListener {
            // writeBoardActivity로 전송하는 코드
           val intent = Intent(context, WriteBoradActivity::class.java)
            registerActivity.launch(intent) // 변경해야됌 일단 걸어논거
        }
    }
}


