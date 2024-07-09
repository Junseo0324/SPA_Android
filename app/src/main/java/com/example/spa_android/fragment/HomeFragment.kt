package com.example.spa_android.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spa_android.Adapter.ProjectListAdapter
import com.example.spa_android.ProjectActivity
import com.example.spa_android.data.ProjectListItem
import com.example.spa_android.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var projectAdapter: ProjectListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        


        binding.projectRv.addOnItemClickListener { _, position, _ ->
            // 프로젝트 클릭 시 ActivityProject로 이동하는 Intent 생성
            val intent = Intent(requireContext(), ActivityProject::class.java)
            startActivity(intent)
        }

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val projectListItemList = arrayListOf(
            ProjectListItem("Sudden Attack", "3", "내용 미리보기1"),
            ProjectListItem("SPA", "2", "내용 미리보기2"),
            ProjectListItem("SBAS", "3", "내용 미리보기3"),
            ProjectListItem("LoL", "4", "내용 미리보기4")
        )
        // 어댑터 생성 및 설정
        projectAdapter = ProjectListAdapter(projectListItemList, object : ProjectListAdapter.OnItemClickListener{
            override fun onItemClick(item: String) {
                val intent = Intent(requireContext(),ProjectActivity::class.java)
                intent.putExtra("projectName",item)
                startActivity(intent)
            }
        })
        binding.projectRv.adapter = projectAdapter

        if (projectListItemList.size <= 2){
            binding.projectRv.layoutManager = LinearLayoutManager(requireContext())
        }
        else {
            //recyclerView를 2x2 로 만들어 주는 역할
            binding.projectRv.layoutManager = GridLayoutManager(context,2)
        }

    }
}