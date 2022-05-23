package com.umc.clone_flo.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umc.clone_flo.view.SongFragment
import com.umc.clone_flo.view.DetailFragment
import com.umc.clone_flo.view.VideoFragment

class AlbumVpAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> SongFragment()
        1 -> DetailFragment()
        else -> VideoFragment()
    }

}