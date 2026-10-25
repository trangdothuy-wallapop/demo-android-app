// Hold name and function to update name

package com.example.myapplication.greeting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GreetingViewModel : ViewModel() {
    var dataClass by mutableStateOf(GreetingDataClass())
        private set

    fun updateName(newName: String) {
        dataClass = dataClass.copy(
            name = newName,
            displayName = if (newName.isBlank()) "Friend" else newName)
    }
}
