package com.example.spa_android.retrofit

data class ApplicantModel(
    var id: Long,
    var boardId: String, //신청한 게시글 ID
    var boardTitle: String,
    var applicants: String, //신청자 이름
    var appliedAt: String, //신청 시간
    var applymotive: String, //신청 동기
    var status: String, //신청상태
    var name: String,
    var role: String
)
