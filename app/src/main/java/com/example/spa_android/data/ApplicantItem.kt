package com.example.spa_android.data

data class ApplicantItem(
    val projectName: String,
    val name: String,
    val content: String,
//    val state: String
    // 0 : 보류  1: 승인  -1: 거절
)
