package com.example.myapplication.tvshow.ui

import com.example.myapplication.tvshow.domain.TvShowError
import com.example.myapplication.tvshow.domain.TvShowModel
import com.example.myapplication.tvshow.domain.TvShowRepository
import com.example.myapplication.tvshow.domain.TvShowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

// Fake repository to control the data returned to the ViewModel
class FakeTvShowRepository(
    var resultToReturn: Result<List<TvShowModel>> = Result.success(emptyList())
) : TvShowRepository {
    override suspend fun getShows(): Result<List<TvShowModel>> = resultToReturn
}

@OptIn(ExperimentalCoroutinesApi::class)
class TvShowViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeTvShowRepository

    @Before
    fun setUp() {
        // Redirect Dispatchers.Main to our test dispatcher before each test
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeTvShowRepository()
    }

    @After
    fun tearDown() {
        // Reset Dispatchers.Main to the original Main dispatcher after each test
        Dispatchers.resetMain()
    }

    // Helper function to create a sample TvShowModel
    private fun createSampleShow(
        id: Int,
        name: String = "Show $id",
        genres: List<String> = listOf("Sci-Fi")): TvShowModel {
        return TvShowModel(
            id = id,
            url = "https://example.com/show/$id",
            name = name,
            yearStart = "2020",
            yearEnd = "2021",
            genres = genres,
            rating = 8.5,
            imageUrl = "https://example.com/show/$id.jpg",
            summary = "Sample summary for show $id"
        )
    }

    @Test
    fun loadShows_successfulFetch_updatesStateWithShows() = runTest {
        // Prepare a list of sample shows
        val sampleShows = listOf(
            createSampleShow(1, "Show 1"),
            createSampleShow(2, "Show 2")
        )
        fakeRepository.resultToReturn = Result.success(sampleShows)
        val useCase = TvShowUseCase(fakeRepository)
        val viewModel = TvShowViewModel(useCase)

        advanceUntilIdle() // Wait for all coroutines to finish

        // Check that the state is updated with the fetched shows
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.errorMessage == null)
        assertEquals(2, state.shows.size)
        assertEquals(sampleShows, state.shows)
    }

    @Test
    fun loadShows_failureFetch_updatesStateWithError() = runTest {
        // Prepare a failure result
        fakeRepository.resultToReturn = Result.failure(TvShowError.NoInternetError)
        val useCase = TvShowUseCase(fakeRepository)
        val viewModel = TvShowViewModel(useCase)

        advanceUntilIdle()

        // Check that the state is updated with the error message
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.errorMessage != null)
        assertEquals("Unable to connect to the server. Please check your internet connection.", state.errorMessage)
    }

    @Test
    fun selectGenre_updatesSelectedGenreInState() = runTest {
        val useCase = TvShowUseCase(fakeRepository)
        val viewModel = TvShowViewModel(useCase)

        advanceUntilIdle()

        // Select a genre
        viewModel.selectGenre("Comedy")

        // Check that the selected genre is updated in the state
        val state = viewModel.uiState.value
        assertEquals("Comedy", state.selectedGenre)
    }

    @Test
    fun toggleLike_updatesLikedShowsInState() = runTest {
        val useCase = TvShowUseCase(fakeRepository)
        val viewModel = TvShowViewModel(useCase)

        advanceUntilIdle()

        // Toggle like for a show
        viewModel.toggleLike(1)
        // Check that the liked show is updated in the state
        assertTrue(viewModel.uiState.value.likedShowIds.contains(1))

        // Toggle like for another show
        viewModel.toggleLike(2)
        assertTrue(viewModel.uiState.value.likedShowIds.contains(2))

        // Toggle unlike
        viewModel.toggleLike(1)
        assertFalse(viewModel.uiState.value.likedShowIds.contains(1))
    }
}