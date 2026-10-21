// Main business logic: Filter 20 valid items from the list of tv shows
package com.example.myapplication.tvshow.domain

import java.io.IOException
// Domain-specific errors representing failures in business logic or data fetching.

sealed class TvShowError(override val message: String) : Exception(message) {
    object NoInternetError : TvShowError("Unable to connect to the server. Please check your internet connection.")
    object EmptyShowsError : TvShowError("No TV shows were returned from the server.")
    object NoValidShowsError : TvShowError("None of the returned TV shows had valid ratings or complete information.")
    data class UnknownError(val throwable: Throwable) : TvShowError(throwable.localizedMessage ?: "An unexpected error occurred.")
}

// Main business logic/rules for TV Shows:
    // 1. Safely fetches from repository and maps network/system exceptions to Domain errors
    // 2. Checks if raw result is empty -> EmptyShowsError
    // 3. Filters to items with valid names, ratings > 0.0, non-blank summaries, and non-blank images
    // 4. Limits to the first 20 items
    // 5. Checks if filtered result is empty -> NoValidShowsError
    // 6. Extracts and sorts distinct genres (with "All" at index 0)

class TvShowUseCase(
    private val repository: TvShowRepository
) {
    suspend operator fun invoke(): Result<TvShowResult> {
        // Rule 1: Safely fetches from repository and maps network/system exceptions to Domain errors
        val repoResult = repository.getShows()

        return repoResult.fold (
            { allShows ->
            // Rule 2: Checks if raw result is empty -> EmptyShowsError
            if (allShows.isEmpty()) {
                return Result.failure(TvShowError.EmptyShowsError)
            }

            // Rule 3: Filters to items with valid names, ratings > 0.0, non-blank summaries, and non-blank images
            val validShows = allShows
                .filter { show ->
                    show.name.isNotBlank() &&
                    show.rating > 0.0 &&
                    show.summary.isNotBlank() &&
                    !show.imageUrl.isNullOrBlank()
                }
                .take(20) // Rule 4: Limits to the first 20 items

            // Rule 5: Checks if filtered result is empty -> NoValidShowsError
            if (validShows.isEmpty()) {
                return Result.failure(TvShowError.NoValidShowsError)
            }

            // Rule 6: Extract distinct genres from these 20 shows (with "All" at index 0)
            val extractedGenres = listOf("All") + validShows
                .flatMap { it.genres }
                .distinct()
                .sorted()

            Result.success(
        TvShowResult(
                shows = validShows,
                genres = extractedGenres
                )
            )
        }, onFailure = { exception ->
            // Map network/system exceptions to Domain errors
            val domainError = when (exception) {
                is IOException -> TvShowError.NoInternetError
                is TvShowError -> exception // Already a domain error
                else -> TvShowError.UnknownError(exception)
            }
            Result.failure(domainError)
            }
        )
    }
}

// Wrapper holding the 20 filtered shows along with their extracted genres.
data class TvShowResult(
    val shows: List<TvShowModel>,
    val genres: List<String>
)