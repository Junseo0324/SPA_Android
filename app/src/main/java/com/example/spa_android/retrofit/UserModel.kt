package com.example.spa_android.retrofit

import java.io.File

data class UserModel(
    var id: Long,
    var filepath: File, //프로필
    var name: String, //이름
    var password: String, //비밀번호
    var email: String, //이메일
    var phone_num: String, //전화번호
    var school: String, //학교
    var year: Int, //학년
    var major: String //전공
)
