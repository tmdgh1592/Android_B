package com.umc.clone_flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.clone_flo.adapter.LockerVpAdapter
import com.umc.clone_flo.databinding.FragmentLockerBinding
import com.umc.clone_flo.util.MusicFileFragment

class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private val information = arrayListOf<String>("저장한 곡", "음악파일")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val albumAdapter = LockerVpAdapter(this@LockerFragment)
            lockerContentVp.adapter = albumAdapter
            TabLayoutMediator(lockerTb, lockerContentVp) { tab, position ->
                tab.text = information[position]
            }.attach()
        }
    }
}