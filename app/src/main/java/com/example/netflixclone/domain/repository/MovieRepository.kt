package com.example.netflixclone.domain.repository

import com.example.netflixclone.domain.model.Movie
import com.example.netflixclone.domain.model.MovieList
import retrofit2.Response

interface MovieRepository {
    suspend fun getMovies(categoryId: Int): Response<MovieList>
}