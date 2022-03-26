package com.umc.clone_flo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.umc.clone_flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.songDownIb.setOnClickListener {
            // 액티비티 종료
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(true)
        }


        binding.songMusicTitleTv.text = intent.getStringExtra("title")
        binding.songSingerNameTv.text = intent.getStringExtra("singer")

    }


    private fun setPlayerStatus(isPlaying : Boolean) {
        if(isPlaying) {
            binding.songMiniplayerIv.visibility = View.VISIBLE // 재생버튼 표시
            binding.songPauseIv.visibility = View.GONE // 정지버튼 없애기
        }
        else
        {
            binding.songMiniplayerIv.visibility = View.GONE // 재생버튼 없애기
            binding.songPauseIv.visibility = View.VISIBLE // 정지버튼 표시
        }
    }

    // menifests 파일에서 activity를 등록해줘야 한다.

}