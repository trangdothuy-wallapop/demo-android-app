// Hold name and function to update name

package com.example.myapplication.greeting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GreetingViewModel : ViewModel() {
    var name by mutableStateOf("Android")
        private set

    val displayName: String
        get() = if (name.isBlank()) "Friend" else name

    fun updateName(newName: String) {
        name = newName
    }
}
