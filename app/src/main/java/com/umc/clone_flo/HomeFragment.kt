package com.umc.clone_flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.clone_flo.adapter.AlbumRvAdapter
import com.umc.clone_flo.databinding.FragmentHomeBinding
import com.umc.clone_flo.util.AlbumAdapterDecoration

class HomeFragment : Fragment() {

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

        with(binding) {

            homePannelAlbumImg01Iv.setOnClickListener {
                val title = binding.homePannelAlbumTitle01Tv.text.toString()
                setFragmentResult("songTitleKey", bundleOf("songTitle" to title))
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, AlbumFragment()).addToBackStack(null)
                    .commitAllowingStateLoss()
            }



            homeTodayMusicAlbumRv.adapter = todayAlbumAdapter
            homeTodayMusicAlbumRv.addItemDecoration(AlbumAdapterDecoration())
            todayAlbums = ArrayList<Song>().apply {
                add(Song("LILAC", "아이유 (IU)", R.drawable.img_album_exp2))
                add(Song("제목", "가수명", R.drawable.img_album_exp))
                add(Song("제목", "가수명", R.drawable.img_album_exp))
                add(Song("제목", "가수명", R.drawable.img_album_exp))
            }
            todayAlbumAdapter.updateList(todayAlbums)


            homeDailyMusicRv.adapter = potcastAdapter
            homeDailyMusicRv.addItemDecoration(AlbumAdapterDecoration())
            dailyAlbums = ArrayList<Song>().apply {
                add(Song("제목", "가수명", R.drawable.img_potcast_exp))
                add(Song("제목", "가수명", R.drawable.img_potcast_exp))
                add(Song("제목", "가수명", R.drawable.img_potcast_exp))
                add(Song("제목", "가수명", R.drawable.img_potcast_exp))
            }
            potcastAdapter.updateList(dailyAlbums)



        }

        return binding.root
    }
}