package com.example.moviebrowser.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites", primaryKeys = ["id", "userId"])
data class MovieEntity(
    val id: Int,
    val userId: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val addedAt: Long = System.currentTimeMillis()
)