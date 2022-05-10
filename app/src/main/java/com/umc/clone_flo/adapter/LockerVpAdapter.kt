package com.umc.clone_flo.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umc.clone_flo.SavedAlbumFragment
import com.umc.clone_flo.SavedMusicFragment
import com.umc.clone_flo.util.MusicFileFragment

class LockerVpAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> SavedAlbumFragment()
        else -> SavedMusicFragment()
    }


}