package com.umc.clone_flo.util

import android.content.Context
import kotlin.math.roundToInt

object UnitConverter {
    fun dpToPx(context: Context, dp: Int): Int =
        (dp * context.resources.displayMetrics.density).roundToInt()

    fun pxToDp(context: Context, px: Int): Int =
        (px / context.resources.displayMetrics.density).roundToInt()
}