package com.example.bingsooflo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerVpAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentList : ArrayList<Fragment> = ArrayList() // 프래그먼트를 담을 리스트
    // 접근 제한자를 붙여 외부에서 사용 못하게 한다.
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
        notifyItemInserted(fragmentList.size-1) // 리스트 안에 새로운 값이 추가되었을때 뷰페이저에게 알려주는 메서드
    }
}