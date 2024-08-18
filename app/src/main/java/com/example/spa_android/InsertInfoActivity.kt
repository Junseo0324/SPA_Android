package com.example.spa_android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityInsertInfoBinding

class InsertInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInsertInfoBinding
    private lateinit var intent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.insertInfoBtn.setOnClickListener {
            intent = Intent(this,ProjectActivity::class.java)
//            showApplyDialog()
            finish()
        }


    }

//    private fun showApplyDialog() {
//        AlertDialog.Builder(this)
//            .setTitle("수정")
//            .setMessage("수정되었습니다.")
//            .setPositiveButton("확인") { dialog, _ ->
//                dialog.dismiss()
//            }
//            .show()
//    }
}