package com.example.netflixclone.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_entity")
data class MovieEntity(

    @PrimaryKey(autoGenerate = false)
    val movieId: Int,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,

    var page: Int = 0
)