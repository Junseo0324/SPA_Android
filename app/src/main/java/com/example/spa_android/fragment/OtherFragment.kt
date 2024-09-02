package com.example.spa_android.fragment

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.spa_android.ApplicantActivity
import com.example.spa_android.InformationActivity
import com.example.spa_android.LoginAndRegister
import com.example.spa_android.databinding.FragmentOtherBinding

class OtherFragment : Fragment() {
    private lateinit var binding: FragmentOtherBinding
    private lateinit var intent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.logoutBtn.setOnClickListener{
            logout()
            intent = Intent(context,LoginAndRegister::class.java)

        }
    }
    private fun logout() {
        val sharedPreferences = requireActivity().getSharedPreferences("MyInformation", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear() // 모든 데이터를 삭제
            apply() // 변경사항을 저장
        }
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
        binding.myInfoConLayout.setOnClickListener {
            intent = Intent(context,InformationActivity::class.java)
            registerActivity.launch(intent)
        }

        binding.projectLinear.setOnClickListener {
            intent = Intent(context,ApplicantActivity::class.java)
            registerActivity.launch(intent) // 변경해야됌 일단 걸어논거
        }
        binding.logoutBtn.setOnClickListener {
            intent = Intent(context,LoginAndRegister::class.java)
            registerActivity.launch(intent)

        }
        binding.deleteBtn.setOnClickListener {
            intent = Intent(context,LoginAndRegister::class.java)
            registerActivity.launch(intent)
            accountdelete()
        }
    }
    private fun accountdelete() {
        // 탈퇴 확인 다이얼로그
        AlertDialog.Builder(requireContext())
            .setTitle("회원 탈퇴")
            .setMessage("정말로 회원탈퇴 하시겠습니까?")
            //.setPositiveButton("예") { _, _ -> deleteAccount() }
            .setNegativeButton("아니오", null)
            .show()
    }
    /*private fun deleteAccount() {
        // 데이터베이스에서 사용자 정보 삭제
        // val userId = getCurrentUserId() // 현재 로그인한 사용자 ID 가져오기

        // val database = DatabaseHelper(requireContext())
         val result = database.deleteUser(userId)

        if (result) {
            Toast.makeText(requireContext(), "회원 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            // 추가적인 작업: 예를 들어, 로그인 화면으로 이동

        } else {
            Toast.makeText(requireContext(), "회원 탈퇴에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }*/
}