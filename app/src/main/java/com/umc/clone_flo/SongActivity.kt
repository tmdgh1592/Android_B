package com.umc.clone_flo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.umc.clone_flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivitySongBinding
    lateinit var song: Song
    lateinit var timer: Timer
    private var gson: Gson = Gson()

    companion object {
        var mediaPlayer: MediaPlayer? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        song = intent.getSerializableExtra("song") as Song
        startTimer()
        setClickListener()
        // 기본 player값을 설정해준다.
        setPlayer()
    }

    private fun setClickListener() {
        with(binding) {
            songDownIb.setOnClickListener(this@SongActivity)
            songMiniplayerIv.setOnClickListener(this@SongActivity)
            songPauseIv.setOnClickListener(this@SongActivity)
            songRandomIv.setOnClickListener(this@SongActivity)
        }
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE // 재생버튼 없애기
            binding.songPauseIv.visibility = View.VISIBLE // 정지버튼 표시
            if(mediaPlayer != null) {
                mediaPlayer?.seekTo(song.pausePosition)
                mediaPlayer?.start()
            }
        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE // 재생버튼 표시
            binding.songPauseIv.visibility = View.GONE // 정지버튼 없애기
            if (mediaPlayer?.isPlaying == true) {
                song.pausePosition = mediaPlayer?.currentPosition ?: 0
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    private fun setPlayer() {
        with(binding) {
            songMusicTitleTv.text = song.title
            songSingerNameTv.text = song.singer
            songStartTimeTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
            songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
            songProgressSb.progress = ((song.mills / song.playTime) * 100).toInt()

            if (mediaPlayer == null) {
                val music = resources.getIdentifier(song.music, "raw", packageName)
                mediaPlayer = MediaPlayer.create(this@SongActivity, music)
            }

            setPlayerStatus(song.isPlaying)
        }
    }

    private fun randomPlay() {
        binding.songProgressSb.progress = 0 // Seekbar progress 0으로 초기화
        binding.songStartTimeTv.text = String.format("%02d:%02d", 0, 0) // 진행 시간 Text 0분 0초로 초기화
        song.second = 0 // 진행 시간 초기화
        song.mills = 0F
        song.isPlaying = true // 바로 실행
        setPlayerStatus(song.isPlaying)

        if (!timer.isInterrupted) {
            timer.interrupt()
        }
        startTimer() // Timer 시작
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean) : Thread() {
        override fun run() {
            super.run()
            try {
                while (true) {
                    if (song.second >= playTime) {
                        runOnUiThread {
                            song.isPlaying = false
                            setPlayerStatus(song.isPlaying)
                            mediaPlayer?.stop()
                        }
                        break
                    }
                    if (isPlaying) {
                        sleep(50)
                        song.mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress =
                                ((song.mills / playTime) * 100).toInt()
                        }

                        if (song.mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.songStartTimeTv.text =
                                    String.format("%02d:%02d", song.second / 60, song.second % 60)
                            }
                            song.second++
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("Song", "Thread is dead.")
            }

        }
    }

    override fun onPause() {
        super.onPause()
        if(!song.isPlaying) {
            setPlayerStatus(false)
        }

        // 현재 음악 상태 저장
        song.pausePosition = mediaPlayer?.currentPosition ?: 0
        val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
        editor.putString("songData", gson.toJson(song)).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.song_down_ib -> finishActivity()
            R.id.song_miniplayer_iv -> setPlayerStatus(true)
            R.id.song_pause_iv -> setPlayerStatus(false)
            R.id.song_random_iv -> randomPlay()
        }
    }

    override fun onBackPressed() {
        finishActivity() // Song data 전달과 함께 종료
    }

    private fun finishActivity() {
        val intent = Intent().putExtra("song", song)
        setResult(RESULT_OK, intent)
        finish()
    }
}