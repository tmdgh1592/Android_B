package com.umc.clone_flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.viewpager2.widget.ViewPager2
import com.umc.clone_flo.adapter.AlbumRvAdapter
import com.umc.clone_flo.adapter.BannerVpAdapter
import com.umc.clone_flo.adapter.PanelVpAdapter
import com.umc.clone_flo.databinding.FragmentHomeBinding
import com.umc.clone_flo.util.AlbumAdapterDecoration
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class HomeFragment : Fragment(), CoroutineScope {

    val job = Job()

    lateinit var binding: FragmentHomeBinding

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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
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
                while(true){
                    delay(2000) // 2초 대기
                    with(homePanelViewpager) {
                        currentItem = (currentItem + 1) % panelAdapter.itemCount
                    }
                }
            }


            homeTodayMusicAlbumRv.adapter = todayAlbumAdapter
            homeTodayMusicAlbumRv.addItemDecoration(AlbumAdapterDecoration())
            todayAlbumAdapter.updateList(todayAlbums)

            homeDailyMusicRv.adapter = potcastAdapter
            homeDailyMusicRv.addItemDecoration(AlbumAdapterDecoration())
            potcastAdapter.updateList(dailyAlbums)

        }
    }

    private fun putDumpData() {
        todayAlbums = ArrayList<Song>().apply {
            add(Song(0, "LILAC", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Song(0, "제목", "가수명", R.drawable.img_album_exp))
            add(Song(0, "제목", "가수명", R.drawable.img_album_exp))
            add(Song(0, "제목", "가수명", R.drawable.img_album_exp))
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