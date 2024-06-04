package com.example.netflixclone.data.mappers

import com.example.netflixclone.data.local.MovieEntity
import com.example.netflixclone.domain.model.Movie

fun Movie.toMovieEntity(): MovieEntity {
    return MovieEntity(
        movieId = movieId,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        page = page
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        movieId = movieId,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        page = page
    )
}