package com.example.netflixclone.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(movieList: List<MovieEntity>)

    @Query("SELECT * FROM movie_entity WHERE movieId = :id")
    suspend fun getMovieById(id: Int): MovieEntity

    @Query("SELECT * FROM movie_entity ORDER BY page")
    fun pagingSource(): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM movie_entity")
    suspend fun clearAll()

//    @Query("SELECT * FROM movie_entity WHERE category")
//    suspend fun getMovieListByCategory(category: String): List<MovieEntity>
}