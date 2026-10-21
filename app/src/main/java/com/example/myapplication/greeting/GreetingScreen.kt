package com.example.myapplication.greeting

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GreetingScreen(viewModel: GreetingViewModel = viewModel()) {
    GreetingUiState(
        name = viewModel.name,
        displayName = viewModel.displayName,
        onNameChange = viewModel::updateName
    )
}



