package com.example.spa_android

import com.example.spa_android.retrofit.BoardModel

interface OnBoardItemClickListener {
    fun deleteBoardItem(id: String, item: BoardModel)
}