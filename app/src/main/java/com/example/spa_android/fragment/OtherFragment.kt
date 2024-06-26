package com.example.spa_android.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.spa_android.ApplicantActivity
import com.example.spa_android.InfomationActivity
import com.example.spa_android.databinding.FragmentOtherBinding

class OtherFragment : Fragment() {
    private lateinit var binding: FragmentOtherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private val registerActivity: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult() // ◀ StartActivityForResult 처리를 담당
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val value = result.data?.getStringExtra("resultData")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtherBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rightBtn.setOnClickListener {
            val intent = Intent(context,InfomationActivity::class.java)
            registerActivity.launch(intent)
        }

        binding.applicantBtn.setOnClickListener {
            val intent = Intent(context,ApplicantActivity::class.java)
            registerActivity.launch(intent) // 변경해야됌 일단 걸어논거
        }
        binding.logoutBtn.setOnClickListener {
            //로그아웃하고 loginAndRegister로 돌아가기
        }


    }
}