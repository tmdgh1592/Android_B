package com.umc.clone_flo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.umc.clone_flo.*
import com.umc.clone_flo.databinding.ActivityMainBinding
import com.umc.clone_flo.util.setStatusBarTransparent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity(override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main) :
    AppCompatActivity(), View.OnClickListener, CoroutineScope {

    private lateinit var activityLauncher: ActivityResultLauncher<Intent>
    lateinit var binding: ActivityMainBinding
    private var song: Song? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Clone_flo)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setStatusBarTransparent(this, binding.rootView)
        setContentView(binding.root)

        setActivityLauncher()
        initSong() // 음악 데이터 초기화
        initBottomNavigation() // 바텀 네비게이션 초기화
        setClickListener() // 클릭 리스너
        startMusic(song!!.playTime) // 음악 시작

//            Log.d("Song", song?.title + song?.singer) // logcat에 이 태그를 출력하는데 나중에 이걸로 검색할 수 있다.

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
    private fun startMusic(playTime: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            while (song!!.second < playTime) {
                // 노래가 실행중이면
                if (song!!.isPlaying) {
                    delay(50)
                    song!!.mills += 50F

                    CoroutineScope(Dispatchers.Main).launch {
                        binding.songProgressSb.progress = ((song!!.mills / playTime) * 100).toInt()
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
            false
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
                val intent = Intent(this@MainActivity, SongActivity::class.java)
                intent.putExtra("song", song)
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
            R.drawable.btn_miniplay_pause
        } else {
            R.drawable.btn_miniplayer_play
        }
        Glide.with(this).load(imgResId).into(binding.mainMiniplayerBtn)
    }
}