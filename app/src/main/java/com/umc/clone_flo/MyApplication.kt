package com.umc.clone_flo

import android.app.Application
import com.umc.clone_flo.view.SongActivity

class MyApplication : Application() {


    override fun onTerminate() {
        super.onTerminate()
        val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
        editor.putString("songData", null).commit()
        SongActivity.mediaPlayer?.release()
        SongActivity.mediaPlayer = null
    }
}