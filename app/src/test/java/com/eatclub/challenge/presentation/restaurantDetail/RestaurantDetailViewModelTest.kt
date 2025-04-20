package com.eatclub.challenge.presentation.restaurantDetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.eatclub.challenge.data.api.model.Deal
import com.eatclub.challenge.data.api.model.Restaurant
import com.eatclub.challenge.di.model.CoroutineDispatchers
import com.eatclub.challenge.presentation.restaurantDetail.RestaurantDetailViewModel.Companion.RESTAURANT_KEY
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RestaurantDetailViewModelTest {
    private lateinit var viewModel: RestaurantDetailViewModel

    @get:Rule
    val mainThreadSurrogate = InstantTaskExecutorRule()

    val coroutineTestDispatcher = StandardTestDispatcher()
    val testCoroutineDispatchers =
        CoroutineDispatchers(
            main = coroutineTestDispatcher,
            io = coroutineTestDispatcher,
            default = coroutineTestDispatcher,
        )

    private val savedStateHandle = mockk<SavedStateHandle>()

    @Before
    fun setup() {
        Dispatchers.setMain(coroutineTestDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `restaurantDetails emits restaurant with deals sorted by descending discount`() =
        runTest {
            // Arrange
            val restaurant =
                Restaurant(
                    name = "Sushi Place",
                    deals =
                        listOf(
                            Deal(discount = 10),
                            Deal(discount = 30),
                            Deal(discount = 20),
                        ),
                )
            // Set up the mock BEFORE creating the ViewModel
            every { savedStateHandle.get<Restaurant>(RESTAURANT_KEY) } returns restaurant
            viewModel = RestaurantDetailViewModel(savedStateHandle, testCoroutineDispatchers)

            // Act
            advanceUntilIdle()

            // Assert
            val result = viewModel.restaurantDetails.value
            val expectedDeals = listOf(Deal(discount = 30), Deal(discount = 20), Deal(discount = 10))
            assertEquals(expectedDeals, result?.deals)
        }

    @Test
    fun `restaurantDetails emits null when restaurant is not found in savedStateHandle`() =
        runTest {
            // Arrange
            every { savedStateHandle.get<Restaurant>(RESTAURANT_KEY) } returns null
            viewModel = RestaurantDetailViewModel(savedStateHandle, testCoroutineDispatchers)

            // Act
            advanceUntilIdle()

            // Assert
            val result = viewModel.restaurantDetails.value
            assertNull(result)
        }

    @Test
    fun `restaurantDetails emits restaurant with empty deals list`() =
        runTest {
            // Arrange
            val restaurant =
                Restaurant(
                    name = "Burger Joint",
                    deals = emptyList(),
                )
            every { savedStateHandle.get<Restaurant>(RESTAURANT_KEY) } returns restaurant
            viewModel = RestaurantDetailViewModel(savedStateHandle, testCoroutineDispatchers)

            // Act
            advanceUntilIdle()

            // Assert
            val result = viewModel.restaurantDetails.value
            assertEquals(emptyList<Deal>(), result?.deals)
        }
}
