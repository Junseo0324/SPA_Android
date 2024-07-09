package com.example.spa_android.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spa_android.data.ProjectListItem
import com.example.spa_android.databinding.ProjectRecyclerBinding

class ProjectListAdapter(private val itemList: ArrayList<ProjectListItem>,private val onItemClickListener: OnItemClickListener):
RecyclerView.Adapter<ProjectListAdapter.ProjectListViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(item: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectListViewHolder {
        val binding = ProjectRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProjectListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ProjectListViewHolder, position: Int) {
        holder.nameText.text = itemList[position].name
        holder.memberText.text = "프로젝트 멤버: ${itemList[position].member}"
        holder.previewText.text = itemList[position].preview
        holder.itemView.setOnClickListener{
            onItemClickListener.onItemClick(itemList[position].name)
        }
    }

    inner class ProjectListViewHolder(private val binding: ProjectRecyclerBinding) : RecyclerView.ViewHolder(binding.root){
        val nameText = binding.projectName
        val memberText = binding.projectMember
        val previewText = binding.projectPreview
    }
}