package com.umc.clone_flo.util

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class RecyclerViewDiffUtil<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem === newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}