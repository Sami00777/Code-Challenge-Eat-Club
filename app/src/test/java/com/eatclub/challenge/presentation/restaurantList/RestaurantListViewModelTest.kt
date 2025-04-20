package com.eatclub.challenge.presentation.restaurantList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.eatclub.challenge.data.api.model.Deal
import com.eatclub.challenge.data.api.model.Restaurant
import com.eatclub.challenge.di.model.CoroutineDispatchers
import com.eatclub.challenge.data.network.NetworkMonitor
import com.eatclub.challenge.domain.common.state.ErrorType
import com.eatclub.challenge.domain.common.state.NetworkRequestState
import com.eatclub.challenge.domain.repository.FetchRestaurantsUseCase
import com.eatclub.challenge.domain.repository.FilterRestaurantsUseCase
import io.mockk.coEvery
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class RestaurantListViewModelTest {
    private lateinit var restaurantListViewModel: RestaurantListViewModel

    @get:Rule
    val mainThreadSurrogate = InstantTaskExecutorRule()

    val coroutineTestDispatcher = StandardTestDispatcher()
    val testCoroutineDispatchers =
        CoroutineDispatchers(
            main = coroutineTestDispatcher,
            io = coroutineTestDispatcher,
            default = coroutineTestDispatcher,
        )

    private val fetchRestaurantsUseCase = mockk<FetchRestaurantsUseCase>()
    private val filterRestaurantsUseCase = mockk<FilterRestaurantsUseCase>()
    private val networkMonitor = mockk<NetworkMonitor>(relaxed = true)
    private val coroutineDispatchers = testCoroutineDispatchers

    private val restaurantWithHighestDeal =
        Restaurant(
            address = "123 Main St",
            cuisines = listOf("Italian", "Pizza"),
            name = "Highest Deal Pizza",
            deals =
                listOf(
                    Deal(
                        discount = 50,
                    ),
                ),
        )

    private val restaurantWithLowestDeal =
        Restaurant(
            address = "101 Pine St",
            cuisines = listOf("Indian"),
            name = "Lowest Deal Curry",
            deals =
                listOf(
                    Deal(
                        discount = 5,
                    ),
                ),
        )

    @Before
    fun setup() {
        Dispatchers.setMain(coroutineTestDispatcher)
        restaurantListViewModel =
            RestaurantListViewModel(
                fetchRestaurantsUseCase = fetchRestaurantsUseCase,
                filterRestaurantsUseCase = filterRestaurantsUseCase,
                dispatchers = coroutineDispatchers,
                networkMonitor = networkMonitor,
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getRestaurantList emits Success when network is available and data is fetched`() =
        runTest {
            // Arrange
            val restaurants =
                listOf(
                    restaurantWithHighestDeal,
                    restaurantWithLowestDeal,
                )

            every { networkMonitor.isNetworkAvailable() } returns true
            coEvery { fetchRestaurantsUseCase() } returns restaurants

            // Act
            restaurantListViewModel.getRestaurantList()
            advanceUntilIdle()

            // Assert
            val restaurantsResult = restaurantListViewModel.filteredRestaurants.value
            assertEquals(NetworkRequestState<List<Restaurant>>.Success(restaurants), restaurantsResult)
        }

    @Test
    fun `getRestaurantList emits Error when network is not available`() =
        runTest {
            // Arrange
            val restaurants =
                listOf(
                    restaurantWithHighestDeal,
                    restaurantWithLowestDeal,
                )

            every { networkMonitor.isNetworkAvailable() } returns false
            coEvery { fetchRestaurantsUseCase() } returns restaurants

            // Act
            restaurantListViewModel.getRestaurantList()
            advanceUntilIdle()

            // Assert
            val restaurantsResult = restaurantListViewModel.filteredRestaurants.value
            assertEquals(
                NetworkRequestState<List<Restaurant>>.Error(ErrorType.UnavailableNetwork("No Internet Connection")),
                restaurantsResult,
            )
        }

    @Test
    fun `getRestaurantList emits Error EmptyResult when API returns empty list`() =
        runTest {
            // Arrange
            every { networkMonitor.isNetworkAvailable() } returns true
            coEvery { fetchRestaurantsUseCase() } returns emptyList()

            // Act
            restaurantListViewModel.getRestaurantList()
            advanceUntilIdle()

            // Assert
            val restaurantsResult = restaurantListViewModel.filteredRestaurants.value
            assertEquals(NetworkRequestState.Error(ErrorType.EmptyResult("No Result Found")), restaurantsResult)
        }

    @Test
    fun `getRestaurantList emits NetworkError when IOException is thrown`() =
        runTest {
            // Arrange
            val networkException = IOException("Network error")
            every { networkMonitor.isNetworkAvailable() } returns true
            coEvery { fetchRestaurantsUseCase() } throws networkException

            // Act
            restaurantListViewModel.getRestaurantList()
            advanceUntilIdle()

            // Assert
            val restaurantsResult = restaurantListViewModel.filteredRestaurants.value
            assertEquals(NetworkRequestState.Error(ErrorType.NetworkError(networkException)), restaurantsResult)
        }

    @Test
    fun `getRestaurantList emits GeneralError when unexpected exception is thrown`() =
        runTest {
            // Arrange
            val networkException = RuntimeException("Something went wrong")
            every { networkMonitor.isNetworkAvailable() } returns true
            coEvery { fetchRestaurantsUseCase() } throws networkException

            // Act
            restaurantListViewModel.getRestaurantList()
            advanceUntilIdle()

            // Assert
            val restaurantsResult = restaurantListViewModel.filteredRestaurants.value
            assertEquals(NetworkRequestState.Error(ErrorType.GeneralError(networkException)), restaurantsResult)
        }

    @Test
    fun `filterRestaurants emits Success when matching results are found`() =
        runTest {
            // Arrange
            val allRestaurants = listOf(restaurantWithHighestDeal, restaurantWithLowestDeal)
            val filteredRestaurants = listOf(restaurantWithHighestDeal)

            every { networkMonitor.isNetworkAvailable() } returns true
            coEvery { fetchRestaurantsUseCase() } returns allRestaurants
            coEvery { filterRestaurantsUseCase("High", allRestaurants) } returns filteredRestaurants

            // Fetch data first
            restaurantListViewModel.getRestaurantList()
            advanceUntilIdle()

            // Act
            restaurantListViewModel.filterRestaurants("High")
            advanceUntilIdle()

            // Assert
            val restaurantsResult = restaurantListViewModel.filteredRestaurants.value
            assertEquals(NetworkRequestState.Success(filteredRestaurants), restaurantsResult)
        }

    @Test
    fun `filterRestaurants emits Error EmptyResult when no match found`() =
        runTest {
            // Arrange
            val allRestaurants = listOf(restaurantWithHighestDeal, restaurantWithLowestDeal)

            every { networkMonitor.isNetworkAvailable() } returns true
            coEvery { fetchRestaurantsUseCase() } returns allRestaurants
            coEvery { filterRestaurantsUseCase("xyz", allRestaurants) } returns emptyList()

            restaurantListViewModel.getRestaurantList()
            advanceUntilIdle()

            // Act
            restaurantListViewModel.filterRestaurants("xyz")
            advanceUntilIdle()

            // Assert
            val restaurantsResult = restaurantListViewModel.filteredRestaurants.value
            assertEquals(NetworkRequestState.Error(ErrorType.EmptyResult("No Result Found for xyz")), restaurantsResult)
        }

    @Test
    fun `resetFilter emits Success with all restaurants`() =
        runTest {
            // Arrange
            val allRestaurants = listOf(restaurantWithHighestDeal, restaurantWithLowestDeal)

            every { networkMonitor.isNetworkAvailable() } returns true
            coEvery { fetchRestaurantsUseCase() } returns allRestaurants

            restaurantListViewModel.getRestaurantList()
            advanceUntilIdle()

            // Act
            restaurantListViewModel.resetFilter()
            advanceUntilIdle()

            // Assert
            val restaurantsResult = restaurantListViewModel.filteredRestaurants.value
            assertEquals(NetworkRequestState.Success(allRestaurants), restaurantsResult)
        }
}
