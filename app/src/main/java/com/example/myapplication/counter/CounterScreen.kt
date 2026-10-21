package com.example.myapplication.counter

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {
    CounterUiState(
        count = viewModel.count,
        onIncrement = viewModel::increment,
        onReset = viewModel::reset
    )
}
