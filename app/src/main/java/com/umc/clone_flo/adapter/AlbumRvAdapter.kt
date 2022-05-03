package com.umc.clone_flo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.clone_flo.Song
import com.umc.clone_flo.databinding.ItemAlbumBinding
import com.umc.clone_flo.util.RecyclerViewDiffUtil

class AlbumRvAdapter(var albumList: ArrayList<Song>) :
    RecyclerView.Adapter<AlbumRvAdapter.AlbumViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(song: Song)
        fun onRemoveAlbum(position: Int)
        fun onPlayClick(song: Song)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    private val mDiffer = AsyncListDiffer(this, RecyclerViewDiffUtil<Song>())

    fun setMyItemClickListener(mItemClickListener: MyItemClickListener) {
        this.mItemClickListener = mItemClickListener
    }

    fun addItem(album: Song) {
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    fun updateList(newList: ArrayList<Song>) {
        mDiffer.submitList(newList)
    }

    inner class AlbumViewHolder(val binding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Song) {
            with(binding) {
                with(item) {
                    itemAlbumTitleTv.text = item.title
                    itemAlbumSingerTv.text = item.singer
                    Glide.with(itemAlbumCoverImgIv).load(coverImg).into(itemAlbumCoverImgIv)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            ItemAlbumBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(albumList[position]) }
        holder.binding.albumPlayIv.setOnClickListener { mItemClickListener.onPlayClick(albumList[position]) }
        //holder.binding.itemAlbumTitleTv.setOnClickListener { mItemClickListener.onRemoveAlbum(position) }
    }

    override fun getItemCount(): Int = mDiffer.currentList.size


}