package com.example.netflixclone.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("Select * From remote_keys Where movie_id = :id")
    suspend fun getRemoteKeyByMovieID(id: Int): RemoteKeys?

    @Query("Delete From remote_keys")
    suspend fun clearRemoteKeys()

    @Query("Select created_at From remote_keys Order By created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long?
}
