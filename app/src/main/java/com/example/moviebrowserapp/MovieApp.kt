package com.example.moviebrowser

import android.app.Application
import com.example.moviebrowser.data.local.MovieDatabase
import com.example.moviebrowser.data.repository.MovieRepository
import com.example.moviebrowser.utils.SessionManager

class MovieApp : Application() {

    val database by lazy { MovieDatabase.getInstance(this) }
    val repository by lazy { MovieRepository(database.movieDao(), database.userDao()) }
    val sessionManager by lazy { SessionManager(this) }
}