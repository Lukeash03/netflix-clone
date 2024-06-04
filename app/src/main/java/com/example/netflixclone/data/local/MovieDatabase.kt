package com.example.netflixclone.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MovieEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun remoteKeysDao(): RemoteKeysDao

}