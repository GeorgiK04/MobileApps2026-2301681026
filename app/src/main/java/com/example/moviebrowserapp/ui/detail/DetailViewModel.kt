package com.example.moviebrowser.ui.detail

import androidx.lifecycle.*
import com.example.moviebrowser.BuildConfig
import com.example.moviebrowser.data.local.MovieEntity
import com.example.moviebrowser.data.remote.MovieDto
import com.example.moviebrowser.data.repository.MovieRepository
import kotlinx.coroutines.launch

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val movie: MovieDto) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}

class DetailViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _uiState = MutableLiveData<DetailUiState>()
    val uiState: LiveData<DetailUiState> = _uiState

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun loadMovie(movieId: Int) {
        _uiState.value = DetailUiState.Loading
        viewModelScope.launch {
            try {
                val movie = repository.getMovieDetails(movieId, BuildConfig.TMDB_API_KEY)
                _uiState.value = DetailUiState.Success(movie)
                _isFavorite.value = repository.isFavorite(movieId)
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error(e.message ?: "Неизвестна грешка")
            }
        }
    }

    fun toggleFavorite(movie: MovieDto) {
        viewModelScope.launch {
            val entity = MovieEntity(
                id = movie.id,
                title = movie.title,
                overview = movie.overview,
                posterPath = movie.posterPath,
                releaseDate = movie.releaseDate,
                voteAverage = movie.voteAverage
            )
            if (_isFavorite.value == true) {
                repository.removeFavorite(entity)
                _isFavorite.value = false
            } else {
                repository.addFavorite(entity)
                _isFavorite.value = true
            }
        }
    }
}

class DetailViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}