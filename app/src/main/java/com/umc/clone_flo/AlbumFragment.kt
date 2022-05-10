package com.umc.clone_flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
class AlbumFragment : Fragment() {
    lateinit var binding: FragmentAlbumBinding// 바인딩 선언
    private val information = arrayListOf<String>("수록곡", "상세정보", "영상")
    private val gson = Gson()

    private var isLiked = MutableLiveData(false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_album, container, false)
        binding.lifecycleOwner = this

        val albumJson = arguments?.getString("album")
        val song = (gson.fromJson(albumJson, Song::class.java) as Song)
        val album = Album(song.id, song.title, song.singer, song.coverImg)

        isLiked.value = isLikedAlbum(album.id)
        setInit(song)
        setClickListeners(album)
        initViewPager()

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            setFragmentResultListener("songTitleKey") { _, bundle ->
                val title = bundle.getString("songTitle")
                binding.albumMusicTitleTv.text = title
            }
            // setClickListener() // 클릭 리스너 설정

            albumBackIv.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeFragment()).commitAllowingStateLoss()
            }

            albumAlbumIv.setOnClickListener {
                Toast.makeText(requireContext(), "LILAC", Toast.LENGTH_SHORT).show()
            }

            isLiked.observe(viewLifecycleOwner) { isHeart ->
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

//    private fun setClickListener() {
//        with(binding) {
//            albumLikeIv.setOnClickListener(this@AlbumFragment)
//        }
//    }

//    private fun toggleLike() {
//        isLike.value = isLike.value?.not()
//    }

    private fun setInit(album: Song?) {
        with(binding) {
            with(album!!) {
                Glide.with(requireContext()).load(coverImg).into(albumAlbumIv)
                albumMusicTitleTv.text = title
                albumSingerNameTv.text = singer
            }

        }
    }


    private fun setClickListeners(album: Album) {
        val userId: Int = getJwt()

        binding.albumLikeIv.setOnClickListener {
            if (isLiked.value!!) {
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
                disLikeAlbum(userId, album.id)
            } else {
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
            }

            isLiked.value = isLiked.value?.not()
        }

        //set click listener
        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }
    }

    private fun initViewPager() {
        //init viewpager
        val albumAdapter = AlbumVPAdapter(this)

        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()
    }

    private fun disLikeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        songDB.albumDao().disLikeAlbum(userId, albumId)
    }

    private fun likeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }


    private fun isLikedAlbum(albumId: Int): Boolean {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        val likeId: Int? = songDB.albumDao().isLikedAlbum(userId, albumId)

        return likeId != null
    }

    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val jwt = spf!!.getInt("jwt", 0)
        Log.d("MAIN_ACT/GET_JWT", "jwt_token: $jwt")

        return jwt
    }
}