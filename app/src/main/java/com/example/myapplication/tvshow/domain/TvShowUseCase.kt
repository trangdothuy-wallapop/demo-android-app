// Main business logic: Filter 20 valid items from the list of tv shows
package com.example.myapplication.tvshow.domain

class TvShowUseCase(
    private val repository: TvShowRepository
) {
    suspend operator fun invoke(): Result<TvShowResult> {
        val repoResult = repository.getShows()

        return repoResult.map { allShows ->
            // Filter out shows with missing/empty fields
            val validShows = allShows
                .filter { show ->
                    show.name.isNotBlank() &&
                    show.rating > 0.0 &&
                    show.summary.isNotBlank() &&
                    !show.imageUrl.isNullOrBlank()
                }
                .take(20) // Take only first 20 valid results

            // Extract distinct genres from these 20 shows
            val extractedGenres = listOf("All") + validShows
                .flatMap { it.genres }
                .distinct()
                .sorted()

            TvShowResult(
                shows = validShows,
                genres = extractedGenres
            )
        }
    }
}

// Wrapper holding the 20 filtered shows along with their extracted genres.
data class TvShowResult(
    val shows: List<TvShowModel>,
    val genres: List<String>
)
