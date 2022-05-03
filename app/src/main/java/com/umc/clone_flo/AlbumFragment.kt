package com.umc.clone_flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.umc.clone_flo.adapter.AlbumVpAdapter
import com.umc.clone_flo.databinding.FragmentAlbumBinding

// Fragment의 기능을 사용할 수 있는 클래스인 Fragment를 상속
class AlbumFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentAlbumBinding// 바인딩 선언
    private val isLike = MutableLiveData<Boolean>(false)
    private val information = arrayListOf<String>("수록곡", "상세정보", "영상")
    private val gson = Gson()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_album, container, false)
        binding.lifecycleOwner = this

        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson, Song::class.java)
        setInit(album)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            setFragmentResultListener("songTitleKey") { _, bundle ->
                val title = bundle.getString("songTitle")
                binding.albumMusicTitleTv.text = title
            }
            setClickListener() // 클릭 리스너 설정

            albumBackIv.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeFragment()).commitAllowingStateLoss()
            }

            albumAlbumIv.setOnClickListener {
                Toast.makeText(requireContext(), "LILAC", Toast.LENGTH_SHORT).show()
            }

            isLike.observe(viewLifecycleOwner) { isHeart ->
                if (isHeart) {
                    albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
                } else {
                    albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
                }
            }

            val albumAdapter = AlbumVpAdapter(this@AlbumFragment)
            albumContentVp.adapter = albumAdapter
            TabLayoutMediator(albumContentTb, albumContentVp) { tab, position ->
                tab.text = information[position]
            }.attach()

        }
    }

    private fun setClickListener() {
        with(binding) {
            albumLikeIv.setOnClickListener(this@AlbumFragment)
        }
    }

    private fun toggleLike() {
        isLike.value = isLike.value?.not()
    }

    private fun setInit(album: Song?) {
        with(binding) {
            with(album!!) {
                Glide.with(requireContext()).load(coverImg).into(albumAlbumIv)
                albumMusicTitleTv.text = title
                albumSingerNameTv.text = singer
            }

        }
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.album_like_iv -> toggleLike()
        }
    }
}