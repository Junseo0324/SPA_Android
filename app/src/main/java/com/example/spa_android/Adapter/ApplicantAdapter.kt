package com.example.spa_android.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spa_android.OnApplicantActionListener
import com.example.spa_android.databinding.ApplicantRecyclerBinding
import com.example.spa_android.retrofit.ApplicantModel

class ApplicantAdapter(
    private val itemList: ArrayList<ApplicantModel>,
    private val actionListener: OnApplicantActionListener):
RecyclerView.Adapter<ApplicantAdapter.ApplicantViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicantAdapter.ApplicantViewHolder {
        val binding = ApplicantRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ApplicantViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ApplicantAdapter.ApplicantViewHolder, position: Int) {
        val applicant = itemList[position]
        holder.projectName.text = itemList[position].boardTitle
        holder.name.text = itemList[position].name
        holder.content.text = itemList[position].applymotive
        holder.role.text = itemList[position].role
        holder.acceptBtn.setOnClickListener {
            actionListener.onAccept(applicant)
        }
        holder.rejectBtn.setOnClickListener {
            actionListener.onReject(applicant)
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
        val role = binding.roleText
    }

}