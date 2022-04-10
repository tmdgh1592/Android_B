package com.umc.clone_flo

import java.io.Serializable

data class Song(
    val musicNumber: Int = 0,
    val title: String = "",
    val singer: String = "",
    val resId: Int? = null,
    val isTitle: Boolean =false,
    val playTime: Int = 0,
    var second: Int = 0,
    var mills: Float = 0F,
    var isPlaying: Boolean = false,
    var music: String = "",
    var pausePosition: Int = 0
): Serializable
