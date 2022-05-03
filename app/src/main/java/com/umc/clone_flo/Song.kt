package com.umc.clone_flo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "SongTable")
data class Song(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val singer: String = "",
    val coverImg: Int? = null,
    val isTitle: Boolean =false,
    var playTime: Int = 0,
    var second: Int = 0,
    var mills: Float = 0F,
    var isPlaying: Boolean = false,
    var music: String = "",
    var pausePosition: Int = 0,
    var isLike: Boolean = false
): Serializable
