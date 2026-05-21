package com.example.moviebrowser

import android.app.Application
import com.example.moviebrowser.data.local.MovieDatabase
import com.example.moviebrowser.data.repository.MovieRepository

class MovieApp : Application() {

    val database by lazy { MovieDatabase.getInstance(this) }
    val repository by lazy { MovieRepository(database.movieDao()) }
}