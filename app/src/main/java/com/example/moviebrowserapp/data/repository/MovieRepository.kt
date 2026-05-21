package com.example.moviebrowser.data.repository

import androidx.lifecycle.LiveData
import com.example.moviebrowser.data.local.MovieDao
import com.example.moviebrowser.data.local.MovieEntity
import com.example.moviebrowser.data.remote.MovieDto
import com.example.moviebrowser.data.remote.RetrofitInstance
import com.example.moviebrowser.data.remote.TmdbApi

class MovieRepository(
    private val movieDao: MovieDao,
    private val api: TmdbApi = RetrofitInstance.api
) {

    // ---- Remote ----

    suspend fun getPopularMovies(apiKey: String) =
        api.getPopularMovies(apiKey)

    suspend fun searchMovies(apiKey: String, query: String) =
        api.searchMovies(apiKey, query)

    suspend fun getMovieDetails(movieId: Int, apiKey: String) =
        api.getMovieDetails(movieId, apiKey)

    // ---- Local (Favorites) ----

    fun getAllFavorites(): LiveData<List<MovieEntity>> =
        movieDao.getAllFavorites()

    suspend fun addFavorite(movie: MovieEntity) =
        movieDao.insertFavorite(movie)

    suspend fun removeFavorite(movie: MovieEntity) =
        movieDao.deleteFavorite(movie)

    suspend fun isFavorite(movieId: Int): Boolean =
        movieDao.isFavorite(movieId) > 0

    suspend fun getFavoriteById(movieId: Int): MovieEntity? =
        movieDao.getFavoriteById(movieId)

    // ---- Mapper: MovieDto → MovieEntity ----

    fun MovieDto.toEntity() = MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage
    )
}