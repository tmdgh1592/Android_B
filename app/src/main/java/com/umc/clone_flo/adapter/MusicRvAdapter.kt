package com.umc.clone_flo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.umc.clone_flo.Song
import com.umc.clone_flo.databinding.ItemMusicBinding
import com.umc.clone_flo.util.RecyclerViewDiffUtil

class MusicRvAdapter(var musicList: ArrayList<Song>) :
    RecyclerView.Adapter<MusicRvAdapter.MusicViewHolder>() {

    private val mDiffer = AsyncListDiffer(this, RecyclerViewDiffUtil<Song>())

    init {
        mDiffer.submitList(musicList)
    }

    inner class MusicViewHolder(val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Song) {
            with(binding) {
                with(item) {
                    val musicNum =
                        if (id.toString().length < 2) "0$id" else id.toString()
                    musicNumberTv.text = musicNum
                    musicTitleTv.text = title
                    musicSingerTv.text = singer
                    titleBadgeView.visibility = if (isTitle) View.VISIBLE else View.GONE
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) =
        holder.bind(musicList[position])

    override fun getItemCount(): Int =
        mDiffer.currentList.size

    fun updateList(newList: ArrayList<Song>) {
        mDiffer.submitList(newList)
    }

}