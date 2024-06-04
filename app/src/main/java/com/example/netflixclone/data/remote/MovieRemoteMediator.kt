package com.example.netflixclone.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.netflixclone.data.local.MovieDatabase
import com.example.netflixclone.data.local.MovieEntity
import com.example.netflixclone.data.local.RemoteKeys
import com.example.netflixclone.data.mappers.toMovieEntity
import com.example.netflixclone.domain.model.Movie
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val movieDb: MovieDatabase,
    private val movieApi: MovieApi
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

        return if (System.currentTimeMillis() - (movieDb.remoteKeysDao().getCreationTime() ?: 0) < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                    prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                    nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
            }

            val response = movieApi.getPopularMovies(page = loadKey)

            if (response.isSuccessful) {

                val movies = response.body()?.results ?: emptyList<Movie>()
                val movieEntity = movies.map { it.toMovieEntity() }
                val endOfPaginationReached = movieEntity.isEmpty()

                movieDb.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        movieDb.remoteKeysDao().clearRemoteKeys()
                        movieDb.movieDao().clearAll()
                    }
                    val prevKey = if (loadKey > 1) loadKey - 1 else null
                    val nextKey = if (endOfPaginationReached) null else loadKey + 1
                    val remoteKeys = movies.map {
                        RemoteKeys(movieID = it.movieId, prevKey = prevKey, currentPage = loadKey, nextKey = nextKey)
                    }

//                    val movieEntities = movies.map { it.toMovieEntity() }
                    movieDb.remoteKeysDao().insertAll(remoteKeys)
                    movieDb.movieDao().insertMovieList(movieEntity.onEachIndexed { _, movie -> movie.page = loadKey})
                }

                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } else {
                MediatorResult.Error(HttpException(response))
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }


    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.movieId?.let { id ->
                movieDb.remoteKeysDao().getRemoteKeyByMovieID(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieEntity>): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { movie ->
            movieDb.remoteKeysDao().getRemoteKeyByMovieID(movie.movieId)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { movie ->
            movieDb.remoteKeysDao().getRemoteKeyByMovieID(movie.movieId)
        }
    }
}