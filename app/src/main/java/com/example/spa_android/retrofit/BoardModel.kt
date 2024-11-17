package com.example.spa_android.retrofit

import java.io.Serializable

data class BoardModel(
    var id: Long,
    var title: String, //게시글 제목
    var content: String, //게시글 내용
    var filePath: String, //업로드 파일
    var timestamp: String, //업로드 시간
    var owner: String, //작성자
    var profile: String,
    var name: String
) : Serializable

data class BoardRequestModel(
    var title: String,
    var content: String,
    var owner: String
)

data class BoardList(
    var id: Long,
    var title: String, //게시글 제목
    var content: String, //게시글 내용
    var filePath: String, //업로드 파일
    var timestamp: String, //업로드 시간
    var owner: String, //작성자
    var name: String,
    var profile: String
)
