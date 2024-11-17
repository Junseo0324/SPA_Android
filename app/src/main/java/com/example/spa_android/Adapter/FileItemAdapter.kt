package com.example.spa_android.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spa_android.OnProjectFileListener
import com.example.spa_android.databinding.FileRecyclerBinding
import com.example.spa_android.retrofit.ProjectContentEntity

class FileItemAdapter(private val itemList: ArrayList<ProjectContentEntity>,private val actionListener: OnProjectFileListener):
RecyclerView.Adapter<FileItemAdapter.FileItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemAdapter.FileItemViewHolder {
        val binding = FileRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FileItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileItemAdapter.FileItemViewHolder, position: Int) {
        holder.name.text = itemList[position].filePath
        holder.content.text = itemList[position].description
        holder.time.text = itemList[position].createdAt
        holder.btn.setOnClickListener {
            actionListener.downloadFile(itemList[position].id)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    inner class FileItemViewHolder(private val binding: FileRecyclerBinding) : RecyclerView.ViewHolder(binding.root){
        val name = binding.fileTitle
        val content = binding.fileContent
        val time = binding.fileTime
        val btn = binding.fileBtn
    }

}