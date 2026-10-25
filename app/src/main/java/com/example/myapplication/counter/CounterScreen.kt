package com.example.myapplication.counter

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {
    CounterUiState(
        dataClass = viewModel.dataClass,
        onIncrement = viewModel::increment,
        onReset = viewModel::reset
    )
}
