package com.umc.clone_flo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.umc.clone_flo.*
import com.umc.clone_flo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val song = Song(binding.songTitleTv.text.toString(), binding.songSingerTv.text.toString(), null)

        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            // intent라는 택배 상자에 데이터라는 물건을 담아서
            intent.putExtra("title", song.title) // "title"이라는 키로 타이틀 담아주기
            intent.putExtra("singer", song.singer) // "singer"라는 키로 가수 이름 담아주기
            startActivity(intent)
        }

        initBottomNavigation()

        Log.d("Song", song.title + song.singer) // logcat에 이 태그를 출력하는데 나중에 이걸로 검색할 수 있다.
    }

    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}