package com.example.moviebrowser.data.repository

import androidx.lifecycle.LiveData
import com.example.moviebrowser.data.local.MovieDao
import com.example.moviebrowser.data.local.MovieEntity
import com.example.moviebrowser.data.local.UserDao
import com.example.moviebrowser.data.local.UserEntity
import com.example.moviebrowser.data.remote.MovieDto
import com.example.moviebrowser.data.remote.RetrofitInstance
import com.example.moviebrowser.data.remote.TmdbApi

class MovieRepository(
    private val movieDao: MovieDao,
    private val userDao: UserDao,
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
    fun getAllFavorites(userId: Int): LiveData<List<MovieEntity>> =
        movieDao.getAllFavorites(userId)

    suspend fun addFavorite(movie: MovieEntity) =
        movieDao.insertFavorite(movie)

    suspend fun removeFavorite(movie: MovieEntity) =
        movieDao.deleteFavorite(movie)

    suspend fun isFavorite(movieId: Int, userId: Int): Boolean =
        movieDao.isFavorite(movieId, userId) > 0

    suspend fun getFavoriteById(movieId: Int, userId: Int): MovieEntity? =
        movieDao.getFavoriteById(movieId, userId)

    // ---- Users ----
    suspend fun register(user: UserEntity): Long =
        userDao.insertUser(user)

    suspend fun login(email: String, password: String): UserEntity? =
        userDao.login(email, password)

    suspend fun getUserByEmail(email: String): UserEntity? =
        userDao.getUserByEmail(email)
}