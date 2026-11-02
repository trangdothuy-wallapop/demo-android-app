package com.example.myapplication.counter

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CounterScreen(viewModel: CounterViewModel = koinViewModel()) {
    CounterUiState(
        dataClass = viewModel.dataClass,
        onIncrement = viewModel::increment,
        onReset = viewModel::reset
    )
}
