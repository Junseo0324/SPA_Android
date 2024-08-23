package com.example.spa_android

import android.content.Context
import androidx.appcompat.app.AlertDialog


object DialogUtils {

    fun showApplyDialog(context: Context,title: String, message: String, onPositiveClick: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
                onPositiveClick()
            }
            .show()
    }
}