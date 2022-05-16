package com.umc.clone_flo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.umc.clone_flo.*
import com.umc.clone_flo.SongActivity.Companion.mediaPlayer
import com.umc.clone_flo.databinding.ActivityMainBinding
import com.umc.clone_flo.util.setStatusBarTransparent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity(override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main) :
    AppCompatActivity(), View.OnClickListener, CoroutineScope {

    private lateinit var activityLauncher: ActivityResultLauncher<Intent>
    lateinit var binding: ActivityMainBinding
    private var song: Song? = null
    private val gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Clone_flo)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setStatusBarTransparent(this, binding.rootView)
        setContentView(binding.root)

        // 더미데이터 삽입
        inputDummySongs()
        inputDummyAlbums()

        setActivityLauncher()
        initSong() // 음악 데이터 초기화
        initBottomNavigation() // 바텀 네비게이션 초기화
        setClickListener() // 클릭 리스너
        startMusic() // 음악 시작
        Log.d("TAG", "onCreate: ${getJwt2()}")

//            Log.d("Song", song?.title + song?.singer) // logcat에 이 태그를 출력하는데 나중에 이걸로 검색할 수 있다.

    }

    private fun getJwt2(): String {
        val spf = getSharedPreferences("auth2", MODE_PRIVATE)
        return spf.getString("jwt", "")!!
    }

    private fun setActivityLauncher() {
        activityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    song = result.data?.getSerializableExtra("song") as Song
                    binding.songProgressSb.progress =
                        ((song!!.mills / song!!.playTime) * 100).toInt()
                    setPlayerStatus(song!!.isPlaying)
                }
            }
    }

    // 실행 버튼 클릭시 Song Model의 isPlaying Flag가 True가 되면서 UI 갱신
    private fun startMusic() {
        CoroutineScope(Dispatchers.Default).launch {
            while (song!!.second < song!!.playTime) {
                // 노래가 실행중이면
                if (song!!.isPlaying) {
                    delay(50)
                    song!!.mills += 50F

                    CoroutineScope(Dispatchers.Main).launch {
                        binding.songProgressSb.progress =
                            ((song!!.mills / song!!.playTime) * 100).toInt()
                    }

                    if (song!!.mills % 1000 == 0F) {
                        song!!.second += 1
                    }
                }
            }
        }
    }

    private fun setClickListener() {
        with(binding) {
            mainPlayerCl.setOnClickListener(this@MainActivity)
            mainMiniplayerBtn.setOnClickListener(this@MainActivity)
        }
    }

    private fun initSong() {
        song = Song(
            0,
            binding.songTitleTv.text.toString(),
            binding.songSingerTv.text.toString(),
            null,
            false,
            60,
            0,
            0f,
            false,
            "lilac"
        )
    }

    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener { item ->
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.main_player_cl -> {
                val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
                editor.putInt("songId", song?.id ?: 0)
                editor.apply()

                val intent = Intent(this@MainActivity, SongActivity::class.java)
                song?.pausePosition = mediaPlayer?.currentPosition ?: 0
                intent.putExtra("songId", song?.id)

                activityLauncher.launch(intent)
                song?.isPlaying = false // 노래 진행을 멈추고 SongActivity에서 처리
            }
            R.id.main_miniplayer_btn -> {
                song?.isPlaying = song?.isPlaying?.not()!!
                setPlayerStatus(song?.isPlaying!!)
            }
        }
    }


    private fun setPlayerStatus(isPlaying: Boolean) {
        song?.isPlaying = isPlaying

        val imgResId = if (isPlaying) {
            if (mediaPlayer?.isPlaying == false) {
                mediaPlayer?.seekTo(song?.pausePosition ?: 0)
                mediaPlayer?.start()
            }
            R.drawable.btn_miniplay_pause
        } else {
            if (mediaPlayer?.isPlaying == true) mediaPlayer?.pause()
            song?.pausePosition = mediaPlayer?.currentPosition ?: 0
            R.drawable.btn_miniplayer_play
        }
        Glide.with(this).load(imgResId).into(binding.mainMiniplayerBtn)
    }

    // 화면이 보여지기 직전에 호출됨
    // onResume은 화면이 보여진 이후에 호출됨.
    override fun onStart() {
        super.onStart()
        val pref = getSharedPreferences("song", MODE_PRIVATE)
        val songId = pref.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!
        song = if (songId == 0) {
            songDB.songDao().getSong(1)
        } else {
            songDB.songDao().getSong(songId)
        }
        /*val songJson = pref.getString("songData", null)

        song = if (songJson == null) {
            Song(
                0,
                "라일락",
                "아이유(IU)",
                null,
                false,
                60,
                0,
                0f,
                false,
                "lilac"
            )
        } else {
            gson.fromJson(songJson, Song::class.java) // json 직렬화
        }*/

        mediaPlayer?.seekTo(song?.pausePosition ?: 0)
    }

    fun playMusic(newSong: Song) {
        with(binding) {
            with(newSong) {
                songTitleTv.text = title
                songSingerTv.text = singer
                song = this
                createMediaPlayer(this)
                setPlayerStatus(true)
            }
        }
    }

    private fun createMediaPlayer(song: Song) {
        if (mediaPlayer == null) {
            val music = resources.getIdentifier(song.music, "raw", packageName)
            mediaPlayer = MediaPlayer.create(this, music)
            mediaPlayer?.seekTo(0)
            mediaPlayer?.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return
        songDB.songDao().insert(
            Song(
                0,
                "LILAC",
                "아이유 (IU)",
                R.drawable.img_album_exp2,
                false,
                279,
                0,
                0F,
                true,
                "lilac",
                0
            )
        )
        songDB.songDao().insert(
            Song(
                0,
                "결론적으로",
                "SPARKY",
                R.drawable.album_sample_01,
                false,
                279,
                0,
                0F,
                true,
                "lilac",
                0
            )
        )
        songDB.songDao().insert(
            Song(
                0,
                "별거 없던 그 하루로",
                "임창정",
                R.drawable.album_sample_02,
                false,
                5,
                0,
                0F,
                true,
                "lilac",
                0
            )
        )
        songDB.songDao().insert(
            Song(0, "잘 가라니", "2am", R.drawable.album_sample_06, false, 5, 0, 0F, true, "lilac", 0)
        )

    }


    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
            Album(
                0,
                "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.img_album_exp2
            )
        )

        songDB.albumDao().insert(
            Album(
                1,
                "Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp
            )
        )

        songDB.albumDao().insert(
            Album(
                2,
                "iScreaM Vol.10 : Next Level Remixes", "에스파 (AESPA)", R.drawable.img_album_exp3
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "MAP OF THE SOUL : PERSONA", "방탄소년단 (BTS)", R.drawable.img_album_exp4
            )
        )

        songDB.albumDao().insert(
            Album(
                4,
                "GREAT!", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5
            )
        )

    }
}