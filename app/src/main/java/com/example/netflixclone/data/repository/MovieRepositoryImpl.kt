package com.example.netflixclone.data.repository

import android.util.Log
import com.example.netflixclone.common.Constants
import com.example.netflixclone.common.Constants.POPULAR_MOVIES
import com.example.netflixclone.common.Constants.TOP_RATED_MOVIES
import com.example.netflixclone.common.Constants.TRENDING_MOVIES
import com.example.netflixclone.common.Constants.UPCOMING_MOVIES
import com.example.netflixclone.data.remote.MovieApi
import com.example.netflixclone.domain.model.MovieList
import com.example.netflixclone.domain.repository.MovieRepository
import retrofit2.Response
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi
) : MovieRepository {
    val TAG = "MovieRepositoryImpl"

    override suspend fun getMovies(categoryId: Int): Response<MovieList> {
        lateinit var response: Response<MovieList>
        when (categoryId) {
            POPULAR_MOVIES -> response = api.getPopularMovies()
            TRENDING_MOVIES -> response = api.getTrendingTodayMovies()
            UPCOMING_MOVIES -> response = api.getUpcomingMovies()
            TOP_RATED_MOVIES -> response = api.getTopRatedMovies()
        }
        return response
    }
}