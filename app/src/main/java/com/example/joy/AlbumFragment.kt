package com.example.joy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.joy.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {
    lateinit var binding: FragmentAlbumBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        // 닫기 누르면 HomeFragment로 전환
        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commitAllowingStateLoss()
        }

        // Toast 메시지 출력
        binding.songLilacLayout.setOnClickListener {
            Toast.makeText(activity, "LILAC", Toast.LENGTH_SHORT).show()
        }
        binding.songSmileyLayout.setOnClickListener {
            Toast.makeText(activity, "SMILEY(Feat. BIBI)", Toast.LENGTH_SHORT).show()
        }
        binding.songRainLayout.setOnClickListener {
            Toast.makeText(activity, "Rain", Toast.LENGTH_SHORT).show()
        }
        binding.songParachuteLayout.setOnClickListener {
            Toast.makeText(activity, "parachute", Toast.LENGTH_SHORT).show()
        }
        binding.songShapeOfYouLayout.setOnClickListener {
            Toast.makeText(activity, "Shape of You", Toast.LENGTH_SHORT).show()
        }
        binding.songPowerUpLayout.setOnClickListener {
            Toast.makeText(activity, "Power Up", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }
}