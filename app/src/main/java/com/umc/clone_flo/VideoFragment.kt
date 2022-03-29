package com.umc.clone_flo

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import com.umc.clone_flo.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {

    lateinit var binding: FragmentVideoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val videoUri = Uri.parse("https://www.youtube.com/watch?v=LKUXc1uwA2Y")
            musicVideoView.setMediaController(MediaController(requireContext()))
            musicVideoView.setVideoURI(videoUri)
            musicVideoView.setOnPreparedListener {
                musicVideoView.requestFocus()
                //musicVideoView.start()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if(binding.musicVideoView.isPlaying) binding.musicVideoView.pause()
    }

    override fun onResume() {
        super.onResume()
        //if(!binding.musicVideoView.isPlaying) binding.musicVideoView.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.musicVideoView.stopPlayback()
    }
}