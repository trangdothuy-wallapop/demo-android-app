package com.example.myapplication.greeting

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun GreetingScreen(viewModel: GreetingViewModel = koinViewModel()) {
    GreetingUiState(
        dataClass = viewModel.dataClass,
        onNameChange = viewModel::updateName
    )
}



