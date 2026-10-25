package com.example.myapplication.counter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CounterViewModel : ViewModel() {
    var dataClass by mutableStateOf(CounterDataClass())
        private set

    fun increment() {
        dataClass = dataClass.copy(count = dataClass.count + 1)
    }

    fun reset() {
        dataClass = dataClass.copy(count = 0)
    }
}