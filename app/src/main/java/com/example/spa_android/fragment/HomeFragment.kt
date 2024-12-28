package com.example.spa_android.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.spa_android.Adapter.ProjectListAdapter
import com.example.spa_android.ProjectActivity
import com.example.spa_android.databinding.FragmentHomeBinding
import com.example.spa_android.retrofit.ProjectListModel
import com.example.spa_android.retrofit.RetrofitApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var projectAdapter: ProjectListAdapter
    private var projectList: ArrayList<ProjectListModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("MyInformation", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null).toString()

        // 어댑터 생성 및 설정
        projectAdapter = ProjectListAdapter(projectList, object : ProjectListAdapter.OnItemClickListener {
            override fun onItemClick(item: String, name: String) {
                val intent = Intent(requireContext(), ProjectActivity::class.java)
                intent.putExtra("selectedProject", item)
                intent.putExtra("selectedProjectName", name)
                startActivity(intent)
            }
        })
        binding.projectRv.adapter = projectAdapter

        binding.projectRv.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        getProjectList(email)
    }

    private fun getProjectList(email: String): ArrayList<ProjectListModel> {
        RetrofitApplication.networkService.getProjectList(email).clone()?.enqueue(object : Callback<ArrayList<ProjectListModel>> {
            override fun onResponse(call: Call<ArrayList<ProjectListModel>>, response: Response<ArrayList<ProjectListModel>>) {
                if (response.isSuccessful) {
                    projectList.clear() // 기존 데이터 삭제
                    projectList.addAll(response.body()!!) // 새로운 데이터 추가
                    projectAdapter.notifyDataSetChanged() // 어댑터에 데이터 변경 알림
                    Log.d(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<ProjectListModel>>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })
        return projectList
    }

    companion object {
        const val TAG = "HomeFragment"
    }
}
