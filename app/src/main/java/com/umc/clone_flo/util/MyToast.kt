package com.umc.clone_flo.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.view.Display
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import com.umc.clone_flo.R
import com.umc.clone_flo.databinding.ToastSampleBinding

object MyToast {

    fun createToast(context: Context, message: String): Toast? {
        val inflater = LayoutInflater.from(context)
        val binding: ToastSampleBinding =
            DataBindingUtil.inflate(inflater, R.layout.toast_sample, null, false)

        binding.tvSample.text = message

        return Toast(context).apply {
            setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM or Gravity.CENTER, 0, 60.toPx())
            duration = Toast.LENGTH_LONG
            view = binding.root
        }
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}