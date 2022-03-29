package com.umc.clone_flo.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umc.clone_flo.SongFragment
import com.umc.clone_flo.DetailFragment
import com.umc.clone_flo.VideoFragment

class AlbumVpAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> SongFragment()
        1 -> DetailFragment()
        else -> VideoFragment()
    }

}