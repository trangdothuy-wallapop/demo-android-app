// Check Dto toDomain() mapping for correctness and null safety
// 1. HTML stripping in summary
// 2. Year extraction: 4-digit years are extracted from "premiered" and "ended" dates
// 3. Null safety fallbacks: When premiered, ended, rating, summary, or image are null, it falls back to defaults without crashing ("N/A", 0.0, "No description available")
// 4. Image fallback: Prefers medium image, falls back to original
// Check JSON deserialization: Verify that raw JSON from the TVMaze API parses into TvShowDto correctly

package com.example.myapplication.tvshow.data

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test


class TvShowDtoTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun toDomain_cleanHtmlTagsAndEntitiesInSummary() {
        val dto = TvShowDto(
            id = 1,
            summary = "<p>A show about <b>cops</b> &amp; robbers, with &#39;drama&#39; and &quot;action&quot;.</p>"
        )

        // Map to domain model
        val model = dto.toDomain()

        // Html tags and entities should be stripped
        assertEquals("A show about cops & robbers, with 'drama' and \"action\".", model.summary)
    }

    @Test
    fun toDomain_extractYearFromPremieredAndEndedDates() {
        val dto = TvShowDto(
            id = 1,
            premiered = "2020-01-01",
            ended = "2021-12-31"
        )

        val model = dto.toDomain()

        assertEquals("2020", model.yearStart)
        assertEquals("2021", model.yearEnd)
    }

    @Test
    fun toDomain_nullSafetyFallbacks() {
        // All optional fields are null
        val dto = TvShowDto(
            id = 1,
            name = null,
            premiered = null,
            ended = null,
            rating = null,
            summary = null,
            image = null
        )

        val model = dto.toDomain()

        assertEquals("", model.name)
        assertEquals("N/A", model.yearStart)
        assertEquals("N/A", model.yearEnd)
        assertEquals(0.0, model.rating, 0.0)
        assertEquals("No description available", model.summary)
        assertNull(model.imageUrl)
    }

    @Test
    fun toDomain_imageFallbackMediumToOriginal() {
        val dtoWithMedium = TvShowDto(
            id = 1,
            image = ImageDto(medium = "medium.jpg", original = "original.jpg")
        )
        val modelWithMedium = dtoWithMedium.toDomain()
        assertEquals("medium.jpg", modelWithMedium.imageUrl)

        val dtoWithoutMedium = TvShowDto(
            id = 2,
            image = ImageDto(medium = null, original = "original.jpg")
        )
        val modelWithoutMedium = dtoWithoutMedium.toDomain()
        assertEquals("original.jpg", modelWithoutMedium.imageUrl)

        val dtoNoImage = TvShowDto(
            id = 3,
            image = null
        )
        val modelNoImage = dtoNoImage.toDomain()
        assertNull(modelNoImage.imageUrl)
    }

    @Test
    fun jsonDeserialization_parsesTvShowDtoCorrectly() {
        val jsonString = """
            {
                "id": 1,
                "url": "https://example.com/show/1",
                "name": "Sample Show",
                "premiered": "2020-01-01",
                "ended": "2021-12-31",
                "genres": ["Drama", "Action"],
                "rating": {"average": 8.5},
                "image": {"medium": "medium.jpg", "original": "original.jpg"},
                "summary": "<p>Sample summary</p>"
            }
        """.trimIndent()

        // Deserialize JSON into TvShowDto
        val dto = json.decodeFromString(TvShowDto.serializer(), jsonString)

        assertEquals(1, dto.id)
        assertEquals("https://example.com/show/1", dto.url)
        assertEquals("Sample Show", dto.name)
        assertEquals("2020-01-01", dto.premiered)
        assertEquals("2021-12-31", dto.ended)
        assertEquals(listOf("Drama", "Action"), dto.genres)
        assertEquals(8.5, dto.rating?.average)
        assertEquals("medium.jpg", dto.image?.medium)
        assertEquals("original.jpg", dto.image?.original)
        assertEquals("<p>Sample summary</p>", dto.summary)
    }
}