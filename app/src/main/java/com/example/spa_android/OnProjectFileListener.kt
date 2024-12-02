package com.example.spa_android

import com.example.spa_android.retrofit.ProjectContentEntity

interface OnProjectFileListener {
    fun downloadFile(id: String)

    fun deleteFile(item: ProjectContentEntity)
}