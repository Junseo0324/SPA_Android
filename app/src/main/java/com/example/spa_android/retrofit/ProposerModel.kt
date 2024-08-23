package com.example.spa_android.retrofit

data class ProposerModel(
    var id: Long,
    var boardId: String, //신청한 게시글 ID
    var applicants: String, //신청자 이름
    var appliedAt: String, //신청 시간
    var applymotve: String, //신청 동기
    var status: String //신청상태
)
