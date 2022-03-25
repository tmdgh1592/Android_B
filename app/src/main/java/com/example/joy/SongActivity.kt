package com.example.joy

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.joy.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    lateinit var binding: ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 닫기 누르면 SongActivity 끝남
        binding.songDownIb.setOnClickListener {
            finish()
        }

        // 재생 -> 중지
        binding.songMinplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }

        // 중지 -> 재생
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        // 미니 플레이어의 제목과 가수가 있는 경우 intent에 담아와서 설정
        if (intent.hasExtra("title") && intent.hasExtra("singer")) {
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")
        }
    }

    fun setPlayerStatus(isPlaying: Boolean) {
        if (isPlaying) {
            binding.songMinplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
        else {
            binding.songMinplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }
    }
}