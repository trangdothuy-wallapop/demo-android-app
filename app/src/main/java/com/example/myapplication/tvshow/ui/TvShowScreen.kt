// Entry Composable for the TV Show tab
// Connects the ViewModel to the stateless UI composable

package com.example.myapplication.tvshow.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TvShowScreen(
    modifier: Modifier = Modifier,
    viewModel: TvShowViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    TvShowUiState(
        uiState = uiState,
        onSelectGenre = viewModel::selectGenre,
        onToggleLike = viewModel::toggleLike,
        onRetry = viewModel::loadShows,
        modifier = modifier
    )
}
