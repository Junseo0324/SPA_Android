package com.example.spa_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spa_android.databinding.ActivityMainBinding
import com.example.spa_android.fragment.BoardFragment
import com.example.spa_android.fragment.ChatListFragment
import com.example.spa_android.fragment.HomeFragment
import com.example.spa_android.fragment.OtherFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportFragmentManager.beginTransaction().add(binding.fg.id,HomeFragment()).commit()
        val nav = binding.bottomNav


//        binding.vp.apply {
//            adapter = ViewPagerAdapater(this@MainActivity)
//            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
//                    binding.bottomNav.selectedItemId = when(position){
//                        0 -> R.id.m_board
//                        1 -> R.id.m_home
//                        else -> R.id.m_chat
//                    }
//                }
//            })
//        }
//        nav.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.m_board -> binding.vp.currentItem = 0
//                R.id.m_home-> binding.vp.currentItem = 1
//                else -> binding.vp.currentItem = 2
//            }
//            true
//        }
        nav.setOnItemSelectedListener {item ->
            when(item.itemId){
                R.id.m_board -> {
                    supportFragmentManager.beginTransaction().replace(binding.fg.id,
                        BoardFragment()
                    ).commit()
                    true

                }
                R.id.m_home -> {
                    supportFragmentManager.beginTransaction().replace(binding.fg.id, HomeFragment()).commit()
                    true
                }
                R.id.m_chat -> {
                    supportFragmentManager.beginTransaction().replace(binding.fg.id,
                        ChatListFragment()
                    ).commit()
                    true
                }
                R.id.m_menu -> {
                    supportFragmentManager.beginTransaction().replace(binding.fg.id,
                        OtherFragment()).commit()
                    true
                }

                else -> false
            }
        }
    }
//    inner class ViewPagerAdapater(fa: FragmentActivity): FragmentStateAdapter(fa){
//        override fun getItemCount(): Int {
//            return 3
//        }
//
//        override fun createFragment(position: Int): Fragment {
//            return when (position){
//                0 -> BoardFragment()
//                1 -> HomeFragment()
//                else -> ChatListFragment()
//            }
//        }
//    }

}