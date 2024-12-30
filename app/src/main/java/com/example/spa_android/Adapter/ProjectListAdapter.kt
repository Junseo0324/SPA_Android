package com.example.spa_android.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spa_android.databinding.ProjectRecyclerBinding
import com.example.spa_android.retrofit.ProjectListModel

class ProjectListAdapter(private val itemList: ArrayList<ProjectListModel>,private val onItemClickListener: OnItemClickListener):
    RecyclerView.Adapter<ProjectListAdapter.ProjectListViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(item: String,name: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectListViewHolder {
        val binding = ProjectRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProjectListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ProjectListViewHolder, position: Int) {
        holder.nameText.text = itemList[position].projectName
        holder.memberText.text = "프로젝트 멤버: ${itemList[position].memberCount}"
        holder.itemView.setOnClickListener{
            onItemClickListener.onItemClick(itemList[position].id,itemList[position].projectName)
        }
    }

    inner class ProjectListViewHolder(private val binding: ProjectRecyclerBinding) : RecyclerView.ViewHolder(binding.root){
        val nameText = binding.projectName
        val memberText = binding.projectMember
    }
}