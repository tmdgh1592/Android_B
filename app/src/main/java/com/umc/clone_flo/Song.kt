package com.umc.clone_flo

import java.io.Serializable

data class Song(
    val musicNumber: Int = 0,
    val title: String = "",
    val singer: String = "",
    val resId: Int? = null,
    val isTitle: Boolean =false
): Serializable
