package com.example.spa_android.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.spa_android.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root

        //recyclerView를 2x2 로 만들어 주는 역할
        binding.projectRv.layoutManager = GridLayoutManager(context,2)
        binding.projectRv.addOnItemClickListener { _, position, _ ->
            // 프로젝트 클릭 시 ActivityProject로 이동하는 Intent 생성
            val intent = Intent(requireContext(), ActivityProject::class.java)
            startActivity(intent)
        }

        return binding.root

    }
}