// Clean domain model for TV Show - only have fields we need (id, url, name, year, genres, rating, image, summary)

package com.example.myapplication.tvshow.domain

data class TvShowModel(
    val id: Int,
    val url: String,
    val name: String,
    val yearStart: String,             // Formatted year extracted from premiered date (e.g., "2015")
    val yearEnd: String,               // Formatted year extracted from ended date (e.g., "2015")
    val genres: List<String>,
    val rating: Double,
    val imageUrl: String?,        // Poster image URL for Coil (medium/original)
    val summary: String,          // Clean plain text summary with HTML tags removed
    val isLiked: Boolean = false  // User's like status
)
