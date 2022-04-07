package com.example.bingsooflo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.bingsooflo.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    var panelData = arrayListOf<Panel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeTodayMusicImg01Iv.setOnClickListener {
            // Fragment 전환
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, AlbumFragment()).commitAllowingStateLoss()
        }

        // Banner ViewPager 2
        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 좌우 스크롤

        // Panel ViewPager 2
        val panelAdapter = PanelVPAdapter(this)
        panelData.add(Panel(R.drawable.img_first_album_default, "매혹적인 음색의 여성 보컬\n팝", "Butter", "BTS", R.drawable.img_album_exp))
        panelData.add(Panel(R.drawable.img_first_album_default, "달밤의 감성 산책", "라일락", "아이유(IU)", R.drawable.img_album_exp2))

        for (position in 0 until panelData.size) {
            panelAdapter.addFragment(PanelFragment(panelData[position]))
        }
        binding.homePanelVp.adapter = panelAdapter
        binding.homePanelVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // connect TabLayout to ViewPager 2
//        TabLayoutMediator(binding.homePanelTb, binding.homePanelVp) {
//                tab, position ->
//            tab.seticon = information[position]
//        }.attach()

        return binding.root
    }
}