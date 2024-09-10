package com.example.spa_android.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spa_android.Adapter.BoardItemAdapter
import com.example.spa_android.LoginAndRegister.Companion.TAG
import com.example.spa_android.WriteBoradActivity
import com.example.spa_android.data.BoardItem
import com.example.spa_android.databinding.FragmentBoardBinding
import com.example.spa_android.retrofit.BoardModel
import com.example.spa_android.retrofit.RetrofitApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BoardFragment : Fragment() {
    private lateinit var binding: FragmentBoardBinding
    private lateinit var boardAdapter: BoardItemAdapter
    var boardList = ArrayList<BoardModel>()
    private val boardCall: Call<ArrayList<BoardModel>> = RetrofitApplication.networkService.getBoardList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getBoardList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        boardAdapter = BoardItemAdapter(boardList)
        binding.boardRecycler.adapter = boardAdapter
        binding.boardRecycler.layoutManager = LinearLayoutManager(requireContext())

        // 버튼 클릭 리스너 설정
        binding.writeBtn.setOnClickListener {
            // writeBoardActivity로 전송하는 코드
            val intent = Intent(context, WriteBoradActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getBoardList(){
        boardCall.clone()?.enqueue(object : Callback<ArrayList<BoardModel>> {
            override fun onResponse(call: Call<ArrayList<BoardModel>>, response: Response<ArrayList<BoardModel>>) {
                if(response.isSuccessful){
                    Log.d(TAG,response.body().toString())
                    boardList = response.body()!!

                }
            }

            override fun onFailure(call: Call<ArrayList<BoardModel>>, t: Throwable) {
                Log.d(TAG,"실패 ${t.message}")
            }
        })
    }
}



