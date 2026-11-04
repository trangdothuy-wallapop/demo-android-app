package com.example.myapplication.tvshow.ui

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import com.karumi.shot.ScreenshotTest
import com.example.myapplication.tvshow.domain.TvShowModel

class TvShowScreenShotTest: ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleShows = listOf(
        TvShowModel(
            id = 1,
            url = "https://example.com/show/1",
            name = "Sample Show 1",
            yearStart = "2020",
            yearEnd = "2021",
            genres = listOf("Drama"),
            isLiked = false,
            rating = 8.5,
            imageUrl = "https://example.com/show/1.jpg",
            summary = "Sample summary for show 1"
        ),
        TvShowModel(
            id = 2,
            url = "https://example.com/show/2",
            name = "Sample Show 2",
            yearStart = "2019",
            yearEnd = "2020",
            genres = listOf("Comedy"),
            isLiked = true,
            rating = 7.8,
            imageUrl = "https://example.com/show/2.jpg",
            summary = "Sample summary for show 2"
        )
    )

    @Test
    fun tvShowScreen_loadState_matchesScreenshot() {
        val uiState = TvShowUiModel(
            isLoading = false,
            shows = sampleShows,
            genres = listOf("All", "Drama", "Comedy"),
            selectedGenre = "All",
            likedShowIds = setOf(2),
            errorState = null
        )

        composeTestRule.setContent {
            TvShowUiState(
                uiState = uiState,
                onSelectGenre = {},
                onToggleLike = {},
                onRetry = {}
            )
        }

        // Capture and compare the screenshot
        compareScreenshot(composeTestRule)
    }

    @Test
    fun tvShowScreen_noInternetErrorState_matchesScreenshot() {
        val errorState = TvShowUiModel(
            isLoading = false,
            shows = emptyList(),
            errorState = TvShowErrorState.NoInternet
        )

        composeTestRule.setContent {
            TvShowUiState(
                uiState = errorState,
                onSelectGenre = {},
                onToggleLike = {},
                onRetry = {}
            )
        }

        // Capture and compare the screenshot
        compareScreenshot(composeTestRule)
    }

    @Test
    fun tvShowScreen_serverDownErrorState_matchesScreenshot() {
        val errorState = TvShowUiModel(
            isLoading = false,
            shows = emptyList(),
            errorState = TvShowErrorState.ServerDown
        )

        composeTestRule.setContent {
            TvShowUiState(
                uiState = errorState,
                onSelectGenre = {},
                onToggleLike = {},
                onRetry = {}
            )
        }

        // Capture and compare the screenshot
        compareScreenshot(composeTestRule)
    }

    @Test
    fun tvShowScreen_emptyShowsErrorState_matchesScreenshot() {
        val errorState = TvShowUiModel(
            isLoading = false,
            shows = emptyList(),
            errorState = TvShowErrorState.EmptyShows
        )

        composeTestRule.setContent {
            TvShowUiState(
                uiState = errorState,
                onSelectGenre = {},
                onToggleLike = {},
                onRetry = {}
            )
        }

        // Capture and compare the screenshot
        compareScreenshot(composeTestRule)
    }

    @Test
    fun tvShowScreen_noValidShowsErrorState_matchesScreenshot() {
        val errorState = TvShowUiModel(
            isLoading = false,
            shows = emptyList(),
            errorState = TvShowErrorState.NoValidShows
        )

        composeTestRule.setContent {
            TvShowUiState(
                uiState = errorState,
                onSelectGenre = {},
                onToggleLike = {},
                onRetry = {}
            )
        }

        // Capture and compare the screenshot
        compareScreenshot(composeTestRule)
    }

    @Test
    fun tvShowScreen_unknownErrorState_matchesScreenshot() {
        val errorState = TvShowUiModel(
            isLoading = false,
            shows = emptyList(),
            errorState = TvShowErrorState.Unknown
        )

        composeTestRule.setContent {
            TvShowUiState(
                uiState = errorState,
                onSelectGenre = {},
                onToggleLike = {},
                onRetry = {}
            )
        }

        // Capture and compare the screenshot
        compareScreenshot(composeTestRule)
    }
}