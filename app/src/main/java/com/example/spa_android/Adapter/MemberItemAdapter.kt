package com.example.spa_android.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spa_android.data.MemberItem
import com.example.spa_android.databinding.MemberRecyclerBinding

class MemberItemAdapter(private val itemList: ArrayList<MemberItem>):
RecyclerView.Adapter<MemberItemAdapter.MemberItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberItemAdapter.MemberItemViewHolder {
        val binding = MemberRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MemberItemViewHolder(binding)
    }
        //val memberName = findViewById<TextView>(R.id.memberName)
    override fun onBindViewHolder(holder: MemberItemAdapter.MemberItemViewHolder, position: Int) {
        holder.name.text = itemList[position].name
        holder.role.text = itemList[position].role
        holder.state.text = itemList[position].state
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    inner class MemberItemViewHolder(private val binding: MemberRecyclerBinding) : RecyclerView.ViewHolder(binding.root){
        val name = binding.memberName // 이름
        val role = binding.memberRole // 역할
        val state = binding.memberState // 상태
//        val image = binding.memberRecyclerImage //이미지
    }

}