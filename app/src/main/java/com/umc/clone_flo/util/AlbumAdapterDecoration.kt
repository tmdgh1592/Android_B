package com.umc.clone_flo.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class AlbumAdapterDecoration: RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        
        val position = parent.getChildAdapterPosition(view)
        val size = state.itemCount
        val offset = UnitConverter.dpToPx(view.context, 20)

        outRect.left = offset
        
        // 마지막 아이템의 경우 오른쪽에도 margin 지정
        if (position == size-1) {
            outRect.right = offset
        }
    }
}