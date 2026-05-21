package com.example.moviebrowser.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {

    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: MovieEntity)

    // READ ALL
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAllFavorites(): LiveData<List<MovieEntity>>

    // READ ONE - за да проверим дали е любим
    @Query("SELECT * FROM favorites WHERE id = :movieId")
    suspend fun getFavoriteById(movieId: Int): MovieEntity?

    // DELETE
    @Delete
    suspend fun deleteFavorite(movie: MovieEntity)

    // Проверка дали е любим (за UI)
    @Query("SELECT COUNT(*) FROM favorites WHERE id = :movieId")
    suspend fun isFavorite(movieId: Int): Int
}