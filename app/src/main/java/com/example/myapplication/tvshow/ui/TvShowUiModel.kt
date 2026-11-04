package com.example.myapplication.tvshow.ui

import com.example.myapplication.tvshow.domain.TvShowModel

// Different UI states for error
sealed interface TvShowErrorState {
    data object NoInternet : TvShowErrorState
    data object ServerDown : TvShowErrorState
    data object EmptyShows : TvShowErrorState
    data object NoValidShows : TvShowErrorState
    data object Unknown: TvShowErrorState
}

// UI State data class holding all presentation data
data class TvShowUiModel(
    val isLoading: Boolean = false,
    val shows: List<TvShowModel> = emptyList(),
    val genres: List<String> = listOf("All"),
    val selectedGenre: String = "All",
    val likedShowIds: Set<Int> = emptySet(),
    val errorState: TvShowErrorState? = null
) {
    // Derived property: returns the list filtered by selected genre
    val displayedShows: List<TvShowModel>
        get() = if (selectedGenre == "All") {
            shows
        } else {
            shows.filter { it.genres.contains(selectedGenre) }
        }
}