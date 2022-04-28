package com.umc.clone_flo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.clone_flo.Song
import com.umc.clone_flo.databinding.ItemSavedMusicBinding
import com.umc.clone_flo.util.RecyclerViewDiffUtil

class LockerAlbumRvAdapter(var albumList: ArrayList<Song>) :
    RecyclerView.Adapter<LockerAlbumRvAdapter.LockerAlbumViewHolder>() {

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(myItemClickListener: MyItemClickListener) {
        this.mItemClickListener = myItemClickListener
    }

    fun removeItem(position: Int) {
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    fun updateList(newList: ArrayList<Song>) {
        albumList = newList
        notifyDataSetChanged()
    }

    interface MyItemClickListener {
        //fun onItemClick(song: Song)
        fun onRemoveAlbum(position: Int)
    }

    inner class LockerAlbumViewHolder(val binding: ItemSavedMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            with(binding) {
                with(song) {
                    Glide.with(albumIv).load(resId).into(albumIv)
                    titleTv.text = song.title
                    singerTv.text = song.singer
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LockerAlbumViewHolder {
        return LockerAlbumViewHolder(
            ItemSavedMusicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LockerAlbumViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.binding.deleteBtn.setOnClickListener { mItemClickListener.onRemoveAlbum(position) }
    }

    override fun getItemCount(): Int = albumList.size

}