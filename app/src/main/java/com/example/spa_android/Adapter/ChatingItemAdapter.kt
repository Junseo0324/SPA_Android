package com.example.spa_android.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.spa_android.data.ChatingItem
import com.example.spa_android.databinding.ChatingRecyclerBinding

class ChatingItemAdapter(private val itemList: ArrayList<ChatingItem>):
RecyclerView.Adapter<ChatingItemAdapter.ChatingItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatingItemAdapter.ChatingItemViewHolder {
        val binding = ChatingRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ChatingItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatingItemAdapter.ChatingItemViewHolder, position: Int) {
        holder.name.text = itemList[position].name
        holder.messsage.text = itemList[position].message

        val params = holder.chatLinear.layoutParams as LinearLayout.LayoutParams

        if(itemList[position].state == "0"){
            params.gravity = android.view.Gravity.END
            holder.name.text = "나"
            holder.chatLinear.gravity = android.view.Gravity.END

        }else{
            params.gravity = android.view.Gravity.START
            holder.chatLinear.gravity = android.view.Gravity.START
        }
        holder.chatLinear.layoutParams = params // 변경된 layoutParams 적용

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ChatingItemViewHolder(private val binding: ChatingRecyclerBinding) : RecyclerView.ViewHolder(binding.root){
        val name = binding.userIdTv
        val messsage = binding.messageTv
        val chatLinear = binding.chatingLinear

    }

}