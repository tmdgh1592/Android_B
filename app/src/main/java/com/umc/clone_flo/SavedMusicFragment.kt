package com.umc.clone_flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.clone_flo.adapter.LockerAlbumRvAdapter
import com.umc.clone_flo.databinding.FragmentSavedMusicBinding

class SavedMusicFragment : Fragment() {
    lateinit var binding: FragmentSavedMusicBinding
    private lateinit var lockerAlbumRvAdapter: LockerAlbumRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lockerAlbumRvAdapter = LockerAlbumRvAdapter(
                arrayListOf<Song>(Song(0,"결론적으로","SPARKY", R.drawable.album_sample_01),
                    Song(1,"별거 없던 그 하루로","임창정", R.drawable.album_sample_02),
                    Song(2,"선물 (White Day) [IDOL (아이돌)]","코튼캔디 (Cotton Candy)", R.drawable.album_sample_03),
                    Song(3,"Bad Boy (PREP Remix)","Red Velvet(레드벨벳)", R.drawable.album_sample_04),
                    Song(4,"Always Me","2am", R.drawable.album_sample_05),
                    Song(5,"잘 가라니","2am", R.drawable.album_sample_06))
            )
            lockerAlbumRv.adapter = lockerAlbumRvAdapter
            lockerAlbumRvAdapter.setMyItemClickListener(object: LockerAlbumRvAdapter.MyItemClickListener {
                override fun onRemoveAlbum(position: Int) {
                    lockerAlbumRvAdapter.removeItem(position)
                }
            })
        }
    }
}