package com.umc.clone_flo.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "AlbumTable")
data class Album(
        @PrimaryKey(autoGenerate = false) var id: Int = 0,
        var title: String? = "",
        var singer: String? = "",
        var coverImg: Int? = null
)
