package com.umc.clone_flo

import androidx.room.*

@Dao
interface SongDao {
    @Insert
    fun insert(song: Song)

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song: Song)

    @Query("SELECT * FROM SongTable")
    fun getSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    fun getSong(id: Int): Song

    @Query("UPDATE SongTable SET isLike = :isLike WHERE id = :id")
    fun updateIsLikeById(id: Int, isLike: Boolean)

    @Query("SELECT * FROM SongTable WHERE isLike = :isLike")
    fun getLikedSongs(isLike: Boolean): List<Song>

    @Query("UPDATE SongTable SET isLike = :isLike")
    fun updateAllIsLike(isLike: Boolean)
}