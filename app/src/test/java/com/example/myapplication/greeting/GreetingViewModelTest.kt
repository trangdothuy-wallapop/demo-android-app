package com.example.myapplication.greeting

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GreetingViewModelTest {
    private lateinit var viewModel: GreetingViewModel

    @Before
    fun setUp() {
        viewModel = GreetingViewModel()
    }

    @Test
    fun initialName_isAndroid() {
        assertEquals("Android", viewModel.name)
    }

    @Test
    fun updateName_changesName() {
        viewModel.updateName("Jetpack Compose")
        assertEquals("Jetpack Compose", viewModel.name)
        assertEquals("Jetpack Compose", viewModel.displayName)
    }

    @Test
    fun emptyOrBlankName_setsDisplayNameToFriend() {
        viewModel.updateName("")
        assertEquals("", viewModel.name)
        assertEquals("Friend", viewModel.displayName)
    }
}