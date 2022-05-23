package com.umc.clone_flo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.umc.clone_flo.*
import com.umc.clone_flo.data.db.SongDatabase
import com.umc.clone_flo.data.entity.Album
import com.umc.clone_flo.data.entity.Song
import com.umc.clone_flo.view.adapter.AlbumRvAdapter
import com.umc.clone_flo.view.adapter.BannerVpAdapter
import com.umc.clone_flo.view.adapter.PanelVpAdapter
import com.umc.clone_flo.databinding.FragmentHomeBinding
import com.umc.clone_flo.util.AlbumAdapterDecoration
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class HomeFragment : Fragment(), CoroutineScope {

    val job = Job()

    lateinit var binding: FragmentHomeBinding
    private var albumData = ArrayList<Album>()

    // 오늘 발매 음악 리사이클러뷰 어댑터
    private var todayAlbums = ArrayList<Song>()
    private val todayAlbumAdapter: AlbumRvAdapter by lazy {
        AlbumRvAdapter(todayAlbums)
    }

    // 매일 들어도 좋은 팟캐스트 리사이클러뷰 어댑터
    private var dailyAlbums = ArrayList<Song>()
    private val potcastAdapter: AlbumRvAdapter by lazy {
        AlbumRvAdapter(dailyAlbums)
    }

    private lateinit var songDB: SongDatabase


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        putDumpData()

        with(binding) {

            homePannelAlbumImg01Iv.setOnClickListener {
                val title = binding.homePannelAlbumTitle01Tv.text.toString()
                setFragmentResult("songTitleKey", bundleOf("songTitle" to title))
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, AlbumFragment()).addToBackStack(null)
                    .commitAllowingStateLoss()
            }

            val bannerAdapter = BannerVpAdapter(this@HomeFragment).apply {
                addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
                addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
                addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
                addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
                addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
                addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
                addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
                addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
                homeBannerVp.adapter = this
                homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            }


            val panelAdapter = PanelVpAdapter(this@HomeFragment).apply {
                addFragment(PanelFragment(R.drawable.img_first_album_default))
                addFragment(PanelFragment(R.drawable.img_first_album_default))
                addFragment(PanelFragment(R.drawable.img_first_album_default))
                addFragment(PanelFragment(R.drawable.img_first_album_default))
                addFragment(PanelFragment(R.drawable.img_first_album_default))
                addFragment(PanelFragment(R.drawable.img_first_album_default))
                addFragment(PanelFragment(R.drawable.img_first_album_default))
                addFragment(PanelFragment(R.drawable.img_first_album_default))
                homePanelViewpager.adapter = this
                homePanelViewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            }
            indicator.setViewPager(homePanelViewpager)
            panelAdapter.registerAdapterDataObserver(indicator.adapterDataObserver)

            launch {
                while (true) {
                    delay(2000) // 2초 대기
                    with(homePanelViewpager) {
                        currentItem = (currentItem + 1) % panelAdapter.itemCount
                    }
                }
            }

            todayAlbumAdapter.setMyItemClickListener(object : AlbumRvAdapter.MyItemClickListener {
                override fun onItemClick(album: Song) {
                    changeAlbumFragment(album)
                }

                override fun onRemoveAlbum(position: Int) {
                    todayAlbumAdapter.removeItem(position)
                }

                override fun onPlayClick(song: Song) {
                    (requireActivity() as MainActivity).playMusic(song)
                }
            })

            albumData.addAll(songDB.albumDao().getAlbums())

            homeTodayMusicAlbumRv.adapter = todayAlbumAdapter
            homeTodayMusicAlbumRv.addItemDecoration(AlbumAdapterDecoration())
            todayAlbumAdapter.updateList(translateData(albumData))


            homeDailyMusicRv.adapter = potcastAdapter
            homeDailyMusicRv.addItemDecoration(AlbumAdapterDecoration())
            potcastAdapter.updateList(translateData(albumData))

        }
    }

    private fun translateData(albumData: ArrayList<Album>): ArrayList<Song> {
        val songList = ArrayList<Song>()
        albumData.forEach { album ->
            songList.add(
                Song(
                    album.id, album.title!!, album.singer!!, album.coverImg,
                    false,
                    279,
                    0,
                    0F,
                    true,
                    "lilac",
                    0
                )
            )
        }

        return songList
    }

    private fun changeAlbumFragment(album: Song) {
        (requireActivity() as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            }).commitAllowingStateLoss()
    }

    private fun putDumpData() {
        todayAlbums = ArrayList<Song>().apply {
            add(
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
            add(Song(0, "결론적으로", "SPARKY", R.drawable.album_sample_01))
            add(Song(0, "별거 없던 그 하루로", "임창정", R.drawable.album_sample_02))
            add(Song(0, "잘 가라니", "2am", R.drawable.album_sample_06))
        }

        dailyAlbums = ArrayList<Song>().apply {
            add(Song(0, "제목", "가수명", R.drawable.img_potcast_exp))
            add(Song(0, "제목", "가수명", R.drawable.img_potcast_exp))
            add(Song(0, "제목", "가수명", R.drawable.img_potcast_exp))
            add(Song(0, "제목", "가수명", R.drawable.img_potcast_exp))
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

}