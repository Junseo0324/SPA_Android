package com.example.spa_android

import com.example.spa_android.retrofit.MemberDTO

interface OnMemberStateChangeListener {
    fun changeState(item: MemberDTO)

    fun newChat(item: MemberDTO)
}