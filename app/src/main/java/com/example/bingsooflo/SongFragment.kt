package com.example.bingsooflo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bingsooflo.databinding.FragmentSongBinding


class SongFragment : Fragment() {

    lateinit var binding: FragmentSongBinding
    lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        // Mix On/Off Event
        binding.songMixSwitchOnIv.setOnClickListener {
            setMixStatus(true)
        }
        binding.songMixSwitchOffIv.setOnClickListener {
            setMixStatus(false)
        }

        // Recylcer View
        val playListData:MutableList<Song> = loadData()
        var adapter = AlbumRVAdapter()
        adapter.listData = playListData
        binding.songPlaylistRv.adapter = adapter
        binding.songPlaylistRv.layoutManager = LinearLayoutManager(mainActivity)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    fun loadData(): MutableList<Song> {
        val data:MutableList<Song> = mutableListOf()
        for (no in 1..6) {
            val title = "제목 $no"
            val singer = "가수 $no"
            var song = Song(title, singer)
            data.add(song)
        }
        return data
    }

    fun setMixStatus(isChecked : Boolean) {
        if(isChecked) {
            binding.songMixSwitchOffIv.visibility = View.VISIBLE
            binding.songMixSwitchOnIv.visibility = View.GONE
        }
        else {
            binding.songMixSwitchOnIv.visibility = View.VISIBLE
            binding.songMixSwitchOffIv.visibility = View.GONE
        }
    }
}

