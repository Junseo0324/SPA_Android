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
data class ProjectListModel(
    var id: String,
    var projectName: String,
    var memberCount: String
)

// TeamProjectDTO
data class TeamProjectDTO(
    val id: Int,
    val projectName: String,
    val members: ArrayList<MemberDTO>?
)

// MemberDTO
data class MemberDTO(
    val id: Long,
    val email: String,
    val memberName: String,
    var conditions: String,
    val role: String,
    val filepath: String
)

data class ContentDTO(
    var author: String,
    var title: String,
    var content: String,
)

data class FileDTO(
    var author: String,
    var description: String
)
data class ProjectContentEntity(
    var id: String,
    var author: String,
    var title: String,
    var content: String,
    var createdAt: String,
    var filePath: String,
    var description: String,
    var fileTime: String
)


