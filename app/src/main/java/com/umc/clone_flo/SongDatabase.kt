package com.umc.clone_flo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Song::class], version = 2)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao

    companion object {
        private var instance: SongDatabase? = null
        private val databaseName = "song-database"

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if (instance == null) {
                synchronized(SongDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        databaseName
                    ).fallbackToDestructiveMigration()
                        .allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }
}