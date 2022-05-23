package com.umc.clone_flo.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.umc.clone_flo.R
import com.umc.clone_flo.data.entity.Song
import com.umc.clone_flo.data.db.SongDatabase
import com.umc.clone_flo.view.adapter.LockerAlbumRvAdapter
import com.umc.clone_flo.databinding.FragmentSavedMusicBinding

class SavedMusicFragment : Fragment() {
    lateinit var binding: FragmentSavedMusicBinding
    private lateinit var lockerAlbumRvAdapter: LockerAlbumRvAdapter
    lateinit var songDB: SongDatabase
    var isAllSelected = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedMusicBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
        with(binding) {
            lockerAlbumRvAdapter = LockerAlbumRvAdapter(
                songDB.songDao().getLikedSongs(true) as ArrayList<Song>
            )

            lockerAlbumRv.adapter = lockerAlbumRvAdapter
            lockerAlbumRvAdapter.setMyItemClickListener(object :
                LockerAlbumRvAdapter.MyItemClickListener {
                override fun onRemoveAlbum(position: Int, songId: Int) {
                    lockerAlbumRvAdapter.removeItem(position)
                    songDB.songDao().updateIsLikeById(songId, false)
                }
            })
        }
    }

    private fun initClickListener() {
        binding.selectAllBtn.setOnClickListener {
            toggleAllSelectedBtn()
        }
    }

    private fun toggleAllSelectedBtn() {
        isAllSelected = !isAllSelected

        if (isAllSelected) {
            binding.selectAllIv.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.select_color
                )
            )
            binding.selectAllTv.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.select_color
                )
            )
            showBottomSheetDialog()
        } else {
            binding.selectAllIv.setColorFilter(Color.parseColor("#858585"))
            binding.selectAllTv.setTextColor(Color.parseColor("#858585"))
        }
    }

    private fun showBottomSheetDialog() {
        val bottomDialog = BottomSheetFragment().apply {
            setOnClickListener(object : BottomSheetFragment.OnItemClickListener {
                override fun onClick(position: Int) {
                    when (position) {
                        3 -> {
                            songDB.songDao().updateAllIsLike(false)
                            lockerAlbumRvAdapter.removeAllItems()
                        }
                    }
                }
            })
            setOnDismissListener(object: BottomSheetFragment.OnDismissListener{
                override fun onDismiss() {
                    toggleAllSelectedBtn()
                }
            })

        }

        bottomDialog.show(parentFragmentManager, bottomDialog.tag)

    }
}