package com.example.spa_android.retrofit

data class ProjcetModel(
    var id: Long,
    var projectName: String, //프로젝트 이름
    var role: String, //멤버 역할
    var condition: String, //멤버 상태
    var title: String, //공지사항 제목
    var content: String, //공지사항 내용
    var timestamp: String, //공지사항 업로드 시간
    var fileName: String, //파일 이름
    var filePath: String, //파일 경로
    var fileTime: String //파일 업로드 시간
)
