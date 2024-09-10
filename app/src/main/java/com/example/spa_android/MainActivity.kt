package com.example.spa_android

import android.os.Bundle
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

        // 초기 Fragment 설정
        replaceFragment(HomeFragment())
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.m_board -> {
                    replaceFragment(BoardFragment())
                    true
                }
                R.id.m_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.m_chat -> {
                    replaceFragment(ChatListFragment())
                    true
                }
                R.id.m_menu -> {
                    replaceFragment(OtherFragment())
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


}