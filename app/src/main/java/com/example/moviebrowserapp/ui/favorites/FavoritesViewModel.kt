package com.example.moviebrowser.ui.favorites

import androidx.lifecycle.*
import com.example.moviebrowser.data.local.MovieEntity
import com.example.moviebrowser.data.repository.MovieRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: MovieRepository,
    private val userId: Int
) : ViewModel() {

    val favorites: LiveData<List<MovieEntity>> = repository.getAllFavorites(userId)

    fun removeFavorite(movie: MovieEntity) {
        viewModelScope.launch {
            repository.removeFavorite(movie)
        }
    }
}


class FavoritesViewModelFactory(
    private val repository: MovieRepository,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(repository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}