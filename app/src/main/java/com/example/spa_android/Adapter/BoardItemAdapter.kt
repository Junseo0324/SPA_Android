package com.example.spa_android.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spa_android.BoardActivity
import com.example.spa_android.EnrollMemberActivity
import com.example.spa_android.databinding.BoardRecyclerBinding
import com.example.spa_android.retrofit.BoardModel

class BoardItemAdapter(private val itemList :ArrayList<BoardModel>):
RecyclerView.Adapter<BoardItemAdapter.BoardItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardItemAdapter.BoardItemViewHolder {
        val binding = BoardRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BoardItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardItemAdapter.BoardItemViewHolder, position: Int) {

        holder.boarditem.setOnClickListener {
            val intent = Intent(holder.itemView.context, BoardActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
        holder.nameText.text = itemList[position].owner
        holder.titleText.text = itemList[position].title
        holder.timeText.text = itemList[position].timestamp
        holder.previewText.text = itemList[position].content
        holder.submitBtn.setOnClickListener {
            val intent = Intent(holder.itemView.context,EnrollMemberActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
//        holder.profileText.text = itemList[position]
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    inner class BoardItemViewHolder(private val binding: BoardRecyclerBinding) : RecyclerView.ViewHolder(binding.root){
        val boarditem = binding.boarditem // 보드전체
        val nameText = binding.boardId //아이디
        val titleText = binding.boardTitle //제목
        val timeText = binding.boardTime // 시간
        val previewText = binding.boardPreview //내용 미리보기
        val submitBtn = binding.submitBtn
//        val profileText = binding.boardPreview //프로필 사진
    }
}