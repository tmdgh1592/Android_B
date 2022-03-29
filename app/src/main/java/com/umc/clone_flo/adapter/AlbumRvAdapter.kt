package com.umc.clone_flo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.clone_flo.Song
import com.umc.clone_flo.databinding.ItemAlbumBinding
import com.umc.clone_flo.util.RecyclerViewDiffUtil

class AlbumRvAdapter(var albumList: ArrayList<Song>) : RecyclerView.Adapter<AlbumRvAdapter.AlbumViewHolder>() {

    private val mDiffer = AsyncListDiffer(this, RecyclerViewDiffUtil<Song>())

    inner class AlbumViewHolder(private val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Song) {
            with(binding) {
                with(item) {
                    itemAlbumTitleTv.text = item.title
                    itemAlbumSingerTv.text = item.singer
                    Glide.with(itemAlbumCoverImgIv).load(resId).into(itemAlbumCoverImgIv)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albumList[position])
    }

    override fun getItemCount(): Int = mDiffer.currentList.size

    fun updateList(newList: ArrayList<Song>) {
        mDiffer.submitList(newList)
    }


}