package com.example.moviebrowser.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: MovieEntity)

    @Query("SELECT * FROM favorites WHERE userId = :userId ORDER BY addedAt DESC")
    fun getAllFavorites(userId: Int): LiveData<List<MovieEntity>>

    @Query("SELECT * FROM favorites WHERE id = :movieId AND userId = :userId")
    suspend fun getFavoriteById(movieId: Int, userId: Int): MovieEntity?

    @Delete
    suspend fun deleteFavorite(movie: MovieEntity)

    @Query("SELECT COUNT(*) FROM favorites WHERE id = :movieId AND userId = :userId")
    suspend fun isFavorite(movieId: Int, userId: Int): Int
}