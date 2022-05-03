package com.umc.clone_flo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.umc.clone_flo.databinding.ActivitySongBinding
import com.umc.clone_flo.util.MyToast

class SongActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivitySongBinding
    lateinit var timer: Timer
    private var gson: Gson = Gson()

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    companion object {
        var mediaPlayer: MediaPlayer? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        setClickListener()
    }

    private fun initSong() {
        val pref = getSharedPreferences("song", MODE_PRIVATE)
        val songId = pref.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)
        Log.d("TAG", "initSong: " + songs[nowPos].id.toString())
        startTimer()
        setPlayer() // 기본 player 값을 설정해준다.
    }

    private fun getPlayingSongPosition(songId: Int): Int {
        for (i in 0 until songs.size) {
            if(songs[i].id == songId) {
                return i
            }
        }
        return 0
    }

    private fun setClickListener() {
        with(binding) {
            songDownIb.setOnClickListener(this@SongActivity)
            songMiniplayerIv.setOnClickListener(this@SongActivity)
            songPauseIv.setOnClickListener(this@SongActivity)
            songRandomIv.setOnClickListener(this@SongActivity)
            songNextIv.setOnClickListener(this@SongActivity)
            songPreviousIv.setOnClickListener(this@SongActivity)
            songLikeIv.setOnClickListener(this@SongActivity)
        }
    }

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying) {
            binding.songMiniplayerIv.visibility = View.GONE // 재생버튼 없애기
            binding.songPauseIv.visibility = View.VISIBLE // 정지버튼 표시
            if (mediaPlayer != null) {
                mediaPlayer?.seekTo(songs[nowPos].pausePosition)
                mediaPlayer?.start()
            }
        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE // 재생버튼 표시
            binding.songPauseIv.visibility = View.GONE // 정지버튼 없애기
            if (mediaPlayer?.isPlaying == true) {
                songs[nowPos].pausePosition = mediaPlayer?.currentPosition ?: 0
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer() {
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }


    private fun createPlayer() {
        if (mediaPlayer == null) {
            val music = resources.getIdentifier(songs[nowPos].music, "raw", packageName)
            mediaPlayer = MediaPlayer.create(this@SongActivity, music)
        }
    }

    private fun setPlayer() {
        with(binding) {
            createPlayer()

            songMusicTitleTv.text = songs[nowPos].title
            songSingerNameTv.text = songs[nowPos].singer
            //songStartTimeTv.text = String.format("%02d:%02d", songs[nowPos].second / 60, songs[nowPos].second % 60)
            songStartTimeTv.text = String.format("%02d:%02d", 0, 0)
            songAlbumIv.setImageResource(songs[nowPos].coverImg!!)
            songEndTimeTv.text = String.format("%02d:%02d", songs[nowPos].playTime / 60, songs[nowPos].playTime % 60)
            songProgressSb.progress = 0

            //songProgressSb.progress = ((songs[nowPos].mills / songs[nowPos].playTime) * 100).toInt()
            //setPlayerStatus(songs[nowPos].isPlaying)

            // 좋아요 이미지 설정
            if (songs[nowPos].isLike) {
                binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
            }else {
                binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
            }

            setPlayerStatus(true)
        }
    }


    private fun randomPlay() {
        binding.songProgressSb.progress = 0 // Seekbar progress 0으로 초기화
        binding.songStartTimeTv.text = String.format("%02d:%02d", 0, 0) // 진행 시간 Text 0분 0초로 초기화
        songs[nowPos].second = 0 // 진행 시간 초기화
        songs[nowPos].mills = 0F
        songs[nowPos].isPlaying = true // 바로 실행
        setPlayerStatus(songs[nowPos].isPlaying)

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
                    if (songs[nowPos].second >= playTime) {
                        runOnUiThread {
                            songs[nowPos].isPlaying = false
                            setPlayerStatus(songs[nowPos].isPlaying)
                            mediaPlayer?.stop()

                            // 다음곡 재생
                            // 마지막 곡인 경우 음악 플레이어 종료
                            if (!moveSong(+1)) {
                                timer.interrupt()
                            }

                        }
                        break
                    }
                    if (isPlaying) {
                        sleep(50)
                        songs[nowPos].mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress =
                                ((songs[nowPos].mills / playTime) * 100).toInt()
                        }

                        if (songs[nowPos].mills % 1000 == 0f) {
                            runOnUiThread {
                                binding.songStartTimeTv.text =
                                    String.format("%02d:%02d", songs[nowPos].second / 60, songs[nowPos].second % 60)
                            }
                            songs[nowPos].second++
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
        if (!songs[nowPos].isPlaying) {
            setPlayerStatus(false)
        }

        // 현재 음악 상태 저장
        songs[nowPos].pausePosition = mediaPlayer?.currentPosition ?: 0
        val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
        editor.putInt("songId", songs[nowPos].id).commit()
    }

    private fun toggleLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(songs[nowPos].id, !isLike)

        if (!isLike) {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
            MyToast.createToast(this, "좋아요 한 곡에 담겼습니다.")?.show()
        }else {
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
            MyToast.createToast(this, "좋아요 한 곡이 취소되었습니다.")?.show()
        }
    }

    private fun moveSong(direct: Int): Boolean {
        if (nowPos + direct < 0) {
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return false
        }
        if (nowPos + direct >= songs.size) {
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return false
        }

        // 다음 곡으로 넘어갈 때, 이전 곡 초기화
        initNowSong()

        nowPos += direct
        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer = null

        setPlayer()

        return true
    }

    private fun initNowSong() {
        with(songs[nowPos]) {
            second = 0
            mills = 0F
            pausePosition = 0
        }
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
            R.id.song_next_iv -> moveSong(+1)
            R.id.song_previous_iv -> moveSong(-1)
            R.id.song_like_iv -> toggleLike(songs[nowPos].isLike)
        }
    }

    override fun onBackPressed() {
        finishActivity() // Song data 전달과 함께 종료
    }

    private fun finishActivity() {
        val intent = Intent().putExtra("song", songs[nowPos])
        setResult(RESULT_OK, intent)
        finish()
    }
}