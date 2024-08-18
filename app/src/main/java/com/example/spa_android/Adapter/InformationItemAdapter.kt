package com.example.spa_android.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spa_android.data.InformationItem
import com.example.spa_android.databinding.InformationRecyclerBinding

class InformationItemAdapter(private val itemList: ArrayList<InformationItem>):
RecyclerView.Adapter<InformationItemAdapter.InformationItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InformationItemAdapter.InformationItemViewHolder {
        val binding = InformationRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return InformationItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InformationItemAdapter.InformationItemViewHolder, position: Int) {
        holder.title.text = itemList[position].title
        holder.content.text = itemList[position].content
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    inner class InformationItemViewHolder(private val binding: InformationRecyclerBinding) : RecyclerView.ViewHolder(binding.root){
        val title = binding.infoTitle
        val content = binding.infoContent
    }

}