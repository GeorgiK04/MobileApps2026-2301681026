package com.example.moviebrowser.ui.home

import androidx.lifecycle.*
import com.example.moviebrowser.BuildConfig
import com.example.moviebrowser.data.remote.MovieDto
import com.example.moviebrowser.data.repository.MovieRepository
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val movies: List<MovieDto>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _uiState = MutableLiveData<HomeUiState>()
    val uiState: LiveData<HomeUiState> = _uiState

    private var currentQuery = ""

    init {
        loadPopularMovies()
    }

    fun loadPopularMovies() {
        currentQuery = ""
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getPopularMovies(BuildConfig.TMDB_API_KEY)
                _uiState.value = HomeUiState.Success(response.results)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Неизвестна грешка")
            }
        }
    }

    fun searchMovies(query: String) {
        if (query.isBlank()) {
            loadPopularMovies()
            return
        }
        currentQuery = query
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.searchMovies(BuildConfig.TMDB_API_KEY, query)
                _uiState.value = HomeUiState.Success(response.results)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Неизвестна грешка")
            }
        }
    }
}

class HomeViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}