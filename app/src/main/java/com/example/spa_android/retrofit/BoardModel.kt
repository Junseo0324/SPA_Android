package com.example.spa_android.retrofit

data class BoardModel(
    var id: Long,
    var title: String, //게시글 제목
    var content: String, //게시글 내용
    var fileName: String, //업로드 파일 이름
    var filePath: String, //업로드 파일
    var timestamp: String //업로드 시간
)

data class BoardRequestModel(
    var title: String,
    var content: String,
    var fileName: String,
    var filePath: String,
    var timestamp: String
)
