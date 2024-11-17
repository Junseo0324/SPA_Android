package com.example.spa_android.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spa_android.BoardActivity
import com.example.spa_android.EnrollMemberActivity
import com.example.spa_android.OnBoardItemClickListener
import com.example.spa_android.R
import com.example.spa_android.databinding.BoardRecyclerBinding
import com.example.spa_android.retrofit.BoardModel
import com.example.spa_android.retrofit.RetrofitApplication

class BoardItemAdapter(private val itemList :ArrayList<BoardModel>,
                       private val actionListener: OnBoardItemClickListener,
                       private val email: String):
RecyclerView.Adapter<BoardItemAdapter.BoardItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardItemAdapter.BoardItemViewHolder {
        val binding = BoardRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BoardItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardItemAdapter.BoardItemViewHolder, position: Int) {

        holder.boarditem.setOnClickListener {
            val intent = Intent(holder.itemView.context, BoardActivity::class.java)
            intent.putExtra("board_item", itemList[position])
            holder.itemView.context.startActivity(intent)
        }
        holder.nameText.text = itemList[position].name
        holder.titleText.text = itemList[position].title
        holder.timeText.text = itemList[position].timestamp
        holder.previewText.text = itemList[position].content
        if (email == itemList[position].owner){
            holder.submitBtn.text = "삭제"
        }
        holder.submitBtn.setOnClickListener {
            if(email == itemList[position].owner){
                actionListener.deleteBoardItem(email,itemList[position])
            }
            else {
                val intent = Intent(holder.itemView.context, EnrollMemberActivity::class.java)
                intent.putExtra("board_item", itemList[position]) // 전체 객체 전달
                holder.itemView.context.startActivity(intent)
            }
        }

        Glide.with(holder.itemView.context)
            .load(RetrofitApplication.BASE_URL + itemList[position].profile)
            .placeholder(R.drawable.sample_user) // 로딩 중에 보여줄 이미지
            .into(holder.profile)
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
        val profile = binding.boardProfile //프로필 사진
    }
}