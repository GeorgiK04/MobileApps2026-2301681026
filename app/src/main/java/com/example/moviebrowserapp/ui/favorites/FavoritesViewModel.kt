package com.example.moviebrowser.ui.favorites

import androidx.lifecycle.*
import com.example.moviebrowser.data.local.MovieEntity
import com.example.moviebrowser.data.repository.MovieRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: MovieRepository) : ViewModel() {

    val favorites: LiveData<List<MovieEntity>> = repository.getAllFavorites()

    fun removeFavorite(movie: MovieEntity) {
        viewModelScope.launch {
            repository.removeFavorite(movie)
        }
    }
}

class FavoritesViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}