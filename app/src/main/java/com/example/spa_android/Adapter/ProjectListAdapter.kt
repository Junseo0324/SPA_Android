package com.example.spa_android.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spa_android.databinding.ProjectRecyclerBinding
import com.example.spa_android.retrofit.ProjectListModel

class ProjectListAdapter(
    private val projectList: ArrayList<ProjectListModel>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: String, name: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        // ViewBinding 객체를 통해 item layout을 인플레이트
        val binding = ProjectRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projectList[position]
        holder.bind(project)
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    // ViewHolder에서 ViewBinding을 사용
    inner class ProjectViewHolder(private val binding: ProjectRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(project: ProjectListModel) {
            // ViewBinding을 통해 projectTitle과 projectDescription을 설정
            binding.projectTitle.text = project.projectName
            binding.projectDescription.text = project.projectDescription

            // 클릭 리스너 설정
            itemView.setOnClickListener {
                listener.onItemClick(project.projectId, project.projectName)
            }
        }
    }
}
