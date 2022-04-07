package com.example.bingsooflo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.bingsooflo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var song: Song
    lateinit var timer : Timer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_BingsooFlo)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer", song.singer)
            intent.putExtra("second", song.second)
            intent.putExtra("playTime", song.playTime)
            intent.putExtra("isPlaying", song.isPlaying)
            startActivity(intent)
        }

        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
        }

        initBottomNavigation()
    }
    private fun initSong() {
        song = Song(
            binding.mainMiniplayerTitleTv.text.toString(),
            binding.mainMiniplayerSingerTv.text.toString(),
            0,
            60,
            false)
        startTimer()
    }
    fun setPlayerStatus(isPlaying : Boolean) {

        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying
        if(isPlaying) {
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
        }
        else {
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
        }
    }

    private fun setPlayer(song : Song) {
        binding.mainMiniplayerProgressSb.progress = (song.second * 1000 / song.playTime)
        setPlayerStatus(song.isPlaying)
    }

    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }


    inner class Timer(private val playTime: Int, var isPlaying: Boolean) : Thread() {
        private var second : Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {
                while(true)
                {
                    if (second >= playTime)
                        break

                    if (isPlaying) {
                        sleep(50)
                        mills += 50
                        runOnUiThread {
                            binding.mainMiniplayerProgressSb.progress = ((mills / playTime) * 100).toInt()
                        }

                        if (mills % 1000 == 0f) {
                            second++
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("Main", "쓰레드가 죽었습니다. ${e.message}")
            }
        }
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