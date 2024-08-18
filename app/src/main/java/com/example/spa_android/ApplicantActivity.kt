package com.example.spa_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spa_android.Adapter.ApplicantAdapter
import com.example.spa_android.data.ApplicantItem
import com.example.spa_android.databinding.ActivityApplicantBinding

class ApplicantActivity : AppCompatActivity() {
    private lateinit var binding: ActivityApplicantBinding
    private lateinit var adapter: ApplicantAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplicantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val applicantList = arrayListOf(
            ApplicantItem("SPA","박성호","하고싶다.."),
            ApplicantItem("Sudden","임채률","화이팅"),
            ApplicantItem("AAA","임채률","ㅋㅋ"),
            ApplicantItem("SBAS","박성호","ㅋㅋㅋ")
        )

        adapter = ApplicantAdapter(applicantList)
        binding.applicantRv.adapter = adapter
        binding.applicantRv.layoutManager = LinearLayoutManager(this)


    }
}