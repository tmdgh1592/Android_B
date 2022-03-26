package com.umc.clone_flo.util

import androidx.recyclerview.widget.DiffUtil

class RecyclerViewDiffUtil<T>(private val oldList: ArrayList<T>, private val newList: ArrayList<T>): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] === newList[newItemPosition]


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

}