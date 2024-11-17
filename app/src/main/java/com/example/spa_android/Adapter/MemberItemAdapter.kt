package com.example.spa_android.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spa_android.OnMemberStateChangeListener
import com.example.spa_android.databinding.MemberRecyclerBinding
import com.example.spa_android.retrofit.MemberDTO

class MemberItemAdapter(private val itemList: ArrayList<MemberDTO>,private val actionListener: OnMemberStateChangeListener):
RecyclerView.Adapter<MemberItemAdapter.MemberItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberItemAdapter.MemberItemViewHolder {
        val binding = MemberRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MemberItemViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MemberItemAdapter.MemberItemViewHolder, position: Int) {
        holder.name.text = itemList[position].memberName
        holder.role.text = itemList[position].role
        holder.state.text = itemList[position].conditions
        //이미지 설정
        // 다이얼로그 클릭 시 상태변경 후 서버에 상태 변경값 전송하기.
        holder.linear.setOnClickListener {
            actionListener.changeState(itemList[position])
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    inner class MemberItemViewHolder(private val binding: MemberRecyclerBinding) : RecyclerView.ViewHolder(binding.root){
        val name = binding.memberName // 이름
        val role = binding.memberRole // 역할
        val state = binding.memberState // 상태
        val linear = binding.memberRvLinear
        val image = binding.memberRecyclerImage //이미지
    }

}