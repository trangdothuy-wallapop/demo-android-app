package com.example.myapplication.counter

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CounterViewModelTest {
    private val viewModel = CounterViewModel()

    @Test
    fun initialCount_isZero() {
        assertEquals(0, viewModel.dataClass.count)
    }

    @Test
    fun increment_increasesCountByOne() {
        viewModel.increment()
        assertEquals(1, viewModel.dataClass.count)

        viewModel.increment()
        assertEquals(2, viewModel.dataClass.count)
    }

    @Test
    fun reset_setsCountToZero() {
        viewModel.increment()
        viewModel.increment()
        assertEquals(2, viewModel.dataClass.count)

        viewModel.reset()
        assertEquals(0, viewModel.dataClass.count)
    }
}