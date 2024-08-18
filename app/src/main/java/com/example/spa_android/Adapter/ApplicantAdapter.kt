package com.example.spa_android.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spa_android.data.ApplicantItem
import com.example.spa_android.databinding.ApplicantRecyclerBinding

class ApplicantAdapter(private val itemList: ArrayList<ApplicantItem>):
RecyclerView.Adapter<ApplicantAdapter.ApplicantViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicantAdapter.ApplicantViewHolder {
        val binding = ApplicantRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ApplicantViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ApplicantAdapter.ApplicantViewHolder, position: Int) {
        holder.projectName.text = itemList[position].projectName
        holder.name.text = itemList[position].name
        holder.content.text = itemList[position].content

        holder.acceptBtn.setOnClickListener {
            // 수락
        }
        holder.rejectBtn.setOnClickListener {
            // 거절
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ApplicantViewHolder(private val binding: ApplicantRecyclerBinding) : RecyclerView.ViewHolder(binding.root){
        val projectName = binding.projectId //프로젝트 이름
        val name = binding.namePj  //신청자
        val content = binding.previewPj //지원동기
        val acceptBtn = binding.approBtn
        val rejectBtn = binding.refuseBtn
    }

}