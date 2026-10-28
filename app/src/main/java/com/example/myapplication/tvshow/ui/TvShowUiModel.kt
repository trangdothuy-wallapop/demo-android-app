package com.example.myapplication.tvshow.ui

import com.example.myapplication.tvshow.domain.TvShowModel

// UI State data class holding all presentation data
data class TvShowUiModel(
    val isLoading: Boolean = false,
    val shows: List<TvShowModel> = emptyList(),
    val genres: List<String> = listOf("All"),
    val selectedGenre: String = "All",
    val likedShowIds: Set<Int> = emptySet(),
    val errorMessage: String? = null
) {
    // Derived property: returns the list filtered by selected genre
    val displayedShows: List<TvShowModel>
        get() = if (selectedGenre == "All") {
            shows
        } else {
            shows.filter { it.genres.contains(selectedGenre) }
        }
}