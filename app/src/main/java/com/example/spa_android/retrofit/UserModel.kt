package com.example.spa_android.retrofit

data class UserModel(
    var id: Long,
    var filepath: String, //프로필
    var name: String, //이름
    var password: String, //비밀번호
    var email: String, //이메일
    var phone_num: String, //전화번호
    var school: String, //학교
    var year: String, //학년
    var major: String//전공
)
data class UserRequestModel(
    val filepath: String? = null,
    val name: String,
    val password: String,
    val email: String,
    val phone_num: String,
    val school: String,
    val year: String,
    val major: String
)
