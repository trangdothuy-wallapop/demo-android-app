// Data Transfer Object (DTO) for TV Show with fields matching API Json response
// Maps to TvShowModel by stripping HTML tags from summary and extracting year from premiered date
// Serialization annotations for JSON parsing
package com.example.myapplication.tvshow.data

import com.example.myapplication.tvshow.domain.TvShowModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// All network fields are nullable or have defaults to avoid crashes if the API changes or returns null.
@Serializable
data class TvShowDto(
    @SerialName("id") val id: Int,
    @SerialName("url") val url: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("ended") val ended: String? = null,           // e.g. "2015-09-10"
    @SerialName("premiered") val premiered: String? = null,   // e.g. "2015-09-10"
    @SerialName("genres") val genres: List<String> = emptyList(),
    @SerialName("rating") val rating: RatingDto? = null,
    @SerialName("image") val image: ImageDto? = null,
    @SerialName("summary") val summary: String? = null        // HTML formatted (e.g., "<p>...</p>")
)

@Serializable
data class RatingDto(
    @SerialName("average") val average: Double? = null
)

@Serializable
data class ImageDto(
    @SerialName("medium") val medium: String? = null,
    @SerialName("original") val original: String? = null
)

// Maps TvShowDto to the clean domain TvShowModel
// This keeps all messy parsing & formatting in the Data layer
fun TvShowDto.toDomain(): TvShowModel {
    // Extract 4-digit year from "premiered"
    val formattedYearPremiere = premiered?.take(4) ?: "N/A"
    val formattedYearEnd = ended?.take(4) ?: "N/A"

    // Strip HTML tags from summary (<p>, <b>, etc.)
    val cleanedSummary = summary
        ?.replace(Regex("<.*?>"), "")
        ?.replace("&amp;", "&")
        ?.replace("&quot;", "\"")
        ?.replace("&#39;", "'")
        ?.trim()
        ?: "No description available"

    return TvShowModel(
        id = id,
        url = url.orEmpty(),
        name = name.orEmpty(),
        yearStart = formattedYearPremiere,
        yearEnd = formattedYearEnd,
        genres = genres,
        rating = rating?.average ?: 0.0,
        imageUrl = image?.medium ?: image?.original,
        summary = cleanedSummary,
        isLiked = false
    )
}
