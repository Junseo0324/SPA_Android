package com.example.spa_android

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.spa_android.databinding.ActivityMainBinding
import com.example.spa_android.fragment.BoardFragment
import com.example.spa_android.fragment.ChatListFragment
import com.example.spa_android.fragment.HomeFragment
import com.example.spa_android.fragment.OtherFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.toolbar.title = "SPA"
        // 초기 Fragment 설정
        replaceFragment(HomeFragment())
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.m_board -> {
                    replaceFragment(BoardFragment())
                    binding.toolbar.title = "게시판"
                    true
                }
                R.id.m_home -> {
                    replaceFragment(HomeFragment())
                    binding.toolbar.title = "SPA"
                    true
                }
                R.id.m_chat -> {
                    replaceFragment(ChatListFragment())
                    binding.toolbar.title = "채팅"
                    true
                }
                R.id.m_menu -> {
                    replaceFragment(OtherFragment())
                    binding.toolbar.title = "설정"
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(binding.fg.id, fragment)
            .commit()
    }
    // 뒤로 가기 버튼 클릭 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // 뒤로 가기 버튼 클릭 시 처리할 동작 (예: 이전 화면으로 돌아감)
                onBackPressedDispatcher.onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}