package com.umc.clone_flo

import java.io.Serializable

data class Song(
    val title: String = "",
    val singer: String = "",
    val resId: Int? = null
): Serializable
