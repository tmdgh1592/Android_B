package com.example.bingsooflo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PanelVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentList : MutableList<Fragment> = mutableListOf()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList.get(position)
    }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
        notifyItemInserted(fragmentList.size-1)
        // notifyDataSetChanged()
    }
}