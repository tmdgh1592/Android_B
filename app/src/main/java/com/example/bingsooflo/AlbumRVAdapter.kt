package com.example.bingsooflo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bingsooflo.databinding.ItemSongBinding

class AlbumRVAdapter : RecyclerView.Adapter<Holder>() {

    var listData = mutableListOf<Song>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val song = listData.get(position)
        holder.setSong(song)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}

class Holder(val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
    // when item Clicked, display Toast text
    init {
        binding.root.setOnClickListener {
            Toast.makeText(binding.root.context, "제목 : ${binding.albumSongTitleTv.text}, " +
                    "가수 : ${binding.albumSongSingerTv.text}", Toast.LENGTH_LONG).show()
        }
    }
    fun setSong(song: Song) {
        binding.albumSongNoTv.text = "01"
        binding.albumSongTitleTv.text = song.title
        binding.albumSongSingerTv.text = song.singer
    }
}