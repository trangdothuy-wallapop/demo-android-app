// View Model to manage UI state, calls TvShowUseCase, and handles user interactions

package com.example.myapplication.tvshow.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.tvshow.data.TvShowRepositoryImplementation
import com.example.myapplication.tvshow.domain.TvShowUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TvShowViewModel(
    private val useCase: TvShowUseCase = TvShowUseCase(TvShowRepositoryImplementation())
) : ViewModel() {

    // Editable, but only inside this ViewModel
    // It starts with isLoading = true --> the screen shows a loading spinner right away
    private val _uiState = MutableStateFlow(TvShowUiState(isLoading = true))
    // Whenever _uiState changes, Compose automatically redraws the screen, but UI isn't allowed to modify uiState directly
    val uiState: StateFlow<TvShowUiState> = _uiState.asStateFlow()

    init {
        loadShows()
    }

    // Loads TV Shows using TvShowUseCase and updates UiState accordingly
    fun loadShows() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = useCase()

            // Unpack from UseCase Result<TvShowResult> and update the UI state accordingly
            result.fold(
                // If API succeeded: stop loading and put shows & genres into UI state
                onSuccess = { data ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            shows = data.shows,
                            genres = data.genres,
                            errorMessage = null
                        )
                    }
                },
                // If API failed: stop loading and show error message
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "Failed to load TV shows. Please check your internet connection."
                        )
                    }
                }
            )
        }
    }

    // Selects a genre to filter the displayed shows
    fun selectGenre(genre: String) {
        _uiState.update { it.copy(selectedGenre = genre) }
    }

    // Toggles the like status of a show (adds or removes from likedShowIds)
    fun toggleLike(showId: Int) {
        _uiState.update { current ->
            val updatedLikes = if (current.likedShowIds.contains(showId)) {
                current.likedShowIds - showId
            } else {
                current.likedShowIds + showId
            }
            current.copy(likedShowIds = updatedLikes)
        }
    }
}
