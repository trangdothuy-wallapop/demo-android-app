package com.example.myapplication.greeting

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GreetingViewModelTest {
    private val viewModel: GreetingViewModel = GreetingViewModel()

    @Test
    fun initialName_isAndroid() {
        assertEquals(GreetingModel(name = "Android", displayName = "Android"), viewModel.dataClass)
    }

    @Test
    fun updateName_changesName() {
        viewModel.updateName("Jetpack Compose")
        assertEquals(GreetingModel(name = "Jetpack Compose", displayName = "Jetpack Compose"), viewModel.dataClass)
    }

    @Test
    fun emptyOrBlankName_setsDisplayNameToFriend() {
        viewModel.updateName("")
        assertEquals(GreetingModel(name = "", displayName = "Friend"), viewModel.dataClass)
    }
}