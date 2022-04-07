package com.example.bingsooflo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bingsooflo.databinding.FragmentMusicBinding


class MusicFragment : Fragment() {
        lateinit var binding: FragmentMusicBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMusicBinding.inflate(inflater, container, false)
        return binding.root
    }
}