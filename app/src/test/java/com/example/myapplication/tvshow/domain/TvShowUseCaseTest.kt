package com.example.myapplication.tvshow.domain

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class FakeTvShowRepository (
    var resultToReturn: Result<List<TvShowModel>> = Result.success(emptyList())
) : TvShowRepository  {
    override suspend fun getShows(): Result<List<TvShowModel>> {
        return resultToReturn
    }
}

class TvShowUseCaseTest {
    private fun createSampleShow(
        id: Int,
        name: String = "Show $id",
        rating: Double = 5.0,
        genres: List<String> = listOf("Drama"),
        imageUrl: String? = "https://example.com/show/$id.jpg",
        summary: String = "Sample summary",
    ): TvShowModel {
        return TvShowModel(
            id = id,
            url = "https://example.com/show/$id",
            name = name,
            rating = rating,
            yearStart = "2020-01-01",
            yearEnd = "2021-12-31",
            genres = genres,
            imageUrl = imageUrl,
            summary = summary,
        )
    }

    @Test
    fun rule1_ioException_returnsNoInternetError() = runBlocking {
        val fakeRepository = FakeTvShowRepository(
            resultToReturn = Result.failure(IOException("No internet"))
        )
        val useCase = TvShowUseCase(fakeRepository)

        val result = useCase()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TvShowError.NoInternetError)
        assertEquals("Unable to connect to the server. Please check your internet connection.", result.exceptionOrNull()?.message)
    }

    @Test
    fun rule2_emptyResult_returnsEmptyShowsError() = runBlocking {
        val fakeRepository = FakeTvShowRepository(
            resultToReturn = Result.success(emptyList())
        )
        val useCase = TvShowUseCase(fakeRepository)

        val result = useCase()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TvShowError.EmptyShowsError)
        assertEquals("No TV shows were returned from the server.", result.exceptionOrNull()?.message)
    }

    @Test
    fun rule3_filterInvalidShows_returnsNoValidShowsError() = runBlocking {
        val invalidShows = listOf(
            createSampleShow(id = 1, name = "", rating = 5.0), // Invalid name
            createSampleShow(id = 2, name = "Valid Show", rating = 0.0), // Invalid rating
            createSampleShow(id = 3, name = "Valid Show", rating = 5.0, summary = ""), // Invalid summary
            createSampleShow(id = 4, name = "Valid Show", rating = 5.0, imageUrl = null) // Invalid image
        )
        val fakeRepository = FakeTvShowRepository(
            resultToReturn = Result.success(invalidShows)
        )
        val useCase = TvShowUseCase(fakeRepository)

        val result = useCase()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TvShowError.NoValidShowsError)
        assertEquals("None of the returned TV shows had valid ratings or complete information.", result.exceptionOrNull()?.message)
    }

    @Test
    fun rule4_limitToFirst20Items_returnsOnly20Shows() = runBlocking {
        val shows = (1..30).map { createSampleShow(id = it) } // 30 valid shows
        val fakeRepository = FakeTvShowRepository(
            resultToReturn = Result.success(shows)
        )
        val useCase = TvShowUseCase(fakeRepository)

        val result = useCase()

        assertTrue(result.isSuccess)
        val tvShowResult = result.getOrNull()
        assertEquals(20, tvShowResult?.shows?.size)
    }

    @Test
    fun rule5_noValidShowsAfterFiltering_returnsNoValidShowsError() = runBlocking {
        val invalidShows = listOf(
            createSampleShow(id = 1, name = "", rating = 5.0), // Invalid name
            createSampleShow(id = 2, name = "Valid Show", rating = 0.0), // Invalid rating
            createSampleShow(id = 3, name = "Valid Show", rating = 5.0, summary = ""), // Invalid summary
            createSampleShow(id = 4, name = "Valid Show", rating = 5.0, imageUrl = null) // Invalid image
        )
        val fakeRepository = FakeTvShowRepository(
            resultToReturn = Result.success(invalidShows)
        )
        val useCase = TvShowUseCase(fakeRepository)

        val result = useCase()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is TvShowError.NoValidShowsError)
        assertEquals("None of the returned TV shows had valid ratings or complete information.", result.exceptionOrNull()?.message)
    }

    @Test
    fun rule6_extractGenres_returnsDistinctSortedGenresWithAll() = runBlocking {
        val shows = listOf(
            createSampleShow(id = 1, genres = listOf("Drama", "Action")),
            createSampleShow(id = 2, genres = listOf("Comedy", "Drama")),
            createSampleShow(id = 3, genres = listOf("Action", "Thriller"))
        )
        val fakeRepository = FakeTvShowRepository(
            resultToReturn = Result.success(shows)
        )
        val useCase = TvShowUseCase(fakeRepository)

        val result = useCase()

        assertTrue(result.isSuccess)
        val tvShowResult = result.getOrNull()
        val expectedGenres = listOf("All", "Action", "Comedy", "Drama", "Thriller")
        assertEquals(expectedGenres, tvShowResult?.genres)
    }

    @Test
    fun validCase_mixedValidAndInvalidShows_returnsOnlyValidShows() = runBlocking {
        val shows = listOf(
            createSampleShow(id = 1, name = "Valid Show 1", rating = 5.0),
            createSampleShow(id = 2, name = "", rating = 5.0), // Invalid name
            createSampleShow(id = 3, name = "Valid Show 2", rating = 0.0), // Invalid rating
            createSampleShow(id = 4, name = "Valid Show 3", rating = 5.0, summary = ""), // Invalid summary
            createSampleShow(id = 5, name = "Valid Show 4", rating = 5.0, imageUrl = null), // Invalid image
            createSampleShow(id = 6, name = "Valid Show 5", rating = 5.0)
        )
        val fakeRepository = FakeTvShowRepository(
            resultToReturn = Result.success(shows)
        )
        val useCase = TvShowUseCase(fakeRepository)

        val result = useCase()

        assertTrue(result.isSuccess)
        val tvShowResult = result.getOrNull()

        // Only valid shows should be returned
        assertEquals(2, tvShowResult?.shows?.size)
        assertEquals(listOf(1, 6), tvShowResult?.shows?.map { it.id })
        assertEquals(listOf("Valid Show 1", "Valid Show 5"), tvShowResult?.shows?.map { it.name })
    }

    @Test
    fun validCase_preservesDataIntegrityAndOrder() = runBlocking {
        val show1 = createSampleShow(
            id = 1,
            name = "Valid Show 1",
            genres = listOf("Drama", "Action"),
            rating = 5.2,
            imageUrl = "https://example.com/show/1.jpg",
            summary = "Sample summary",
        )
        val show2 = createSampleShow(
            id = 2,
            name = "Valid Show 2",
            genres = listOf("Comedy"),
            rating = 5.3,
            imageUrl = "https://example.com/show/2.jpg",
            summary = "Sample summary 2",
        )

        val shows = listOf(show1, show2)
        val fakeRepository = FakeTvShowRepository(
            resultToReturn = Result.success(shows)
        )
        val useCase = TvShowUseCase(fakeRepository)

        val result = useCase()

        assertTrue(result.isSuccess)
        val tvShowResult = result.getOrThrow()

        // Check size, order of shows
        assertEquals(shows, tvShowResult.shows)

        // Check that the data integrity is preserved
        assertEquals(2, tvShowResult.shows.size)
        assertEquals("Valid Show 1", tvShowResult.shows[0].name)
        assertEquals(5.2, tvShowResult.shows[0].rating, 0.01)
        assertEquals(listOf("Drama", "Action"), tvShowResult.shows[0].genres)

        assertEquals("Valid Show 2", tvShowResult.shows[1].name)
        assertEquals(5.3, tvShowResult.shows[1].rating, 0.01)
        assertEquals(listOf("Comedy"), tvShowResult.shows[1].genres)
    }
}