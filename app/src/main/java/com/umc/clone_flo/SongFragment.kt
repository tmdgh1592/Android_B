package com.umc.clone_flo

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.umc.clone_flo.adapter.MusicRvAdapter
import com.umc.clone_flo.databinding.FragmentSongBinding

class SongFragment: Fragment(), View.OnClickListener {

    lateinit var binding: FragmentSongBinding
    private lateinit var musicRvAdapter: MusicRvAdapter
    private val isAllSelected = MutableLiveData(false)
    private val isAllListen = MutableLiveData(false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setClickListener()
        setLiveData()
    }

    private fun setClickListener() {
        with(binding) {
            allListenBtn.setOnClickListener(this@SongFragment)
            allSelectBtn.setOnClickListener(this@SongFragment)
        }
    }

    private fun setLiveData() {
        with(binding) {
            isAllSelected.observe(viewLifecycleOwner) { isSelected ->
                if (isSelected) {
                    allSelectIv.setImageResource(R.drawable.btn_playlist_select_on)
                    allSelectTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                } else {
                    allSelectIv.setImageResource(R.drawable.btn_playlist_select_off)
                    allSelectTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.unSelectedColor
                        )
                    )
                }
            }

            isAllListen.observe(viewLifecycleOwner) { isSelected ->
                if (isSelected) {
                    allListenIv.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                    allListenTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorPrimary
                        )
                    )
                } else {
                    allListenIv.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.unSelectedColor
                        )
                    )
                    allListenTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.unSelectedColor
                        )
                    )
                }
            }
        }
    }

    private fun setAdapter() {
        musicRvAdapter = MusicRvAdapter(arrayListOf(
            Song(1, "Comin'At Ya (feat.Chillin Hommie)", "Rohann(이로한)", null, true),
            Song(2, "Vague Flow", "Rohann(이로한)"),
            Song(3, "Dial (feat.Deepflow, dsel)", "Rohann(이로한)"),
            Song(4, "Comin'At Ya (feat.Chillin Hommie)", "Rohann(이로한)", null, true),
            Song(5, "Vague Flow", "Rohann(이로한)"),
            Song(6, "Dial (feat.Deepflow, dsel)", "Rohann(이로한)"),
            Song(7, "Comin'At Ya (feat.Chillin Hommie)", "Rohann(이로한)", null, true),
        ))
        binding.musicRv.adapter = musicRvAdapter
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.all_listen_btn -> {
                isAllListen.value = isAllListen.value?.not()
            }
            R.id.all_select_btn -> {
                isAllSelected.value = isAllSelected.value?.not()
            }
        }
    }
}