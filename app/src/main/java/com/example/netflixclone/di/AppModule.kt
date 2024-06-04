package com.example.netflixclone.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.example.netflixclone.common.Constants
import com.example.netflixclone.data.local.MovieDatabase
import com.example.netflixclone.data.local.MovieEntity
import com.example.netflixclone.data.remote.MovieApi
import com.example.netflixclone.data.remote.MovieRemoteMediator
import com.example.netflixclone.data.repository.MovieRepositoryImpl
import com.example.netflixclone.domain.repository.MovieRepository
import com.example.netflixclone.domain.usecase.getmovies.GetPopularMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieApi(): MovieApi {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movies.db"
        ).build()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideMoviePager(movieDb: MovieDatabase, movieApi: MovieApi): Pager<Int, MovieEntity> {
        return Pager(
            config = PagingConfig(pageSize = 15),
            remoteMediator = MovieRemoteMediator(
                movieDb = movieDb,
                movieApi = movieApi
            ),
            pagingSourceFactory = {
                movieDb.movieDao().pagingSource()
            }
        )
    }

    @Provides
    @Singleton
    fun provideMovieRepository(api: MovieApi): MovieRepository {
        return MovieRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideGetPopularMoviesUseCase(movieRepository: MovieRepository): GetPopularMoviesUseCase {
        return GetPopularMoviesUseCase(movieRepository)
    }

}