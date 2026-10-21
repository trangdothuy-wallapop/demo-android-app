// Interface contract for the TvShowRepository in Domain layer.
// Domain layer specifies what data it needs, and Data layer fulfills it.
// Uses Kotlin's built-in Result<T> to safely encapsulate success or failure.

package com.example.myapplication.tvshow.domain

interface TvShowRepository {
    suspend fun getShows(): Result<List<TvShowModel>>
}
