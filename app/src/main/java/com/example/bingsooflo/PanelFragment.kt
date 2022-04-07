package com.example.bingsooflo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bingsooflo.databinding.FragmentPanelBinding


class PanelFragment(val panelData: Panel) : Fragment() {
    lateinit var binding: FragmentPanelBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPanelBinding.inflate(inflater, container, false)

        binding.homePanelTitleTv.text = panelData.title
        binding.homePanelBackgroundIv.setImageResource(panelData.backgroundImage)
        binding.homePanelAlbumImgIv.setImageResource(panelData.songImage)
        binding.homePanelAlbumTitleTv.text = panelData.songTitle
        binding.homePanelAlbumSingerTv.text = panelData.songSinger

        return binding.root
    }
}