package com.eatclub.challenge.domain.usecase

import com.eatclub.challenge.data.api.model.Deal
import com.eatclub.challenge.data.api.model.Restaurant
import com.eatclub.challenge.data.api.model.Restaurants
import com.eatclub.challenge.domain.repository.RestaurantRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FetchRestaurantsUseCaseImplTest {

    private lateinit var fetchRestaurantsUseCase: FetchRestaurantsUseCaseImpl
    private lateinit var restaurantRepository: RestaurantRepository

    val restaurantWithBestDeal = Restaurant(
        name = "Restaurant A",
        deals = listOf(
            Deal(discount = 60),
            Deal(discount = 30)
        )
    )

    val restaurantWithGoodDeal = Restaurant(
        name = "Restaurant B",
        deals = listOf(
            Deal(discount = 40),
            Deal(discount = 20)
        )
    )

    val restaurantWithPoorDeal = Restaurant(
        name = "Restaurant C",
        deals = listOf(
            Deal(discount = 10),
            Deal(discount = 5)
        )
    )

    val restaurantWithEmptyDeal = Restaurant(
        name = "Restaurant D",
        deals = emptyList()
    )

    val restaurantWithNullDeal = Restaurant(
        name = "Restaurant E",
        deals = null
    )

    val restaurantWithMultipleUnsortedDeals = Restaurant(
        name = "Restaurant F",
        deals = listOf(
            Deal(discount = 0),
            Deal(discount = 50),
            Deal(discount = 25),
        )
    )

    val restaurantWithHighestDealLowerSecondDeal = Restaurant(
        name = "Restaurant G",
        deals = listOf(
            Deal(discount = 20),
            Deal(discount = 60)
        )
    )

    @Before
    fun setUp() {
        restaurantRepository = mockk<RestaurantRepository>(relaxed = true)
        fetchRestaurantsUseCase = FetchRestaurantsUseCaseImpl(restaurantRepository)
    }

    @Test
    fun `Successful fetch and sort`() = runTest {
        // Arrange
        val restaurantsResponse = Restaurants(
            listOf(
                restaurantWithBestDeal,
                restaurantWithGoodDeal,
                restaurantWithPoorDeal
            )
        )

        coEvery { restaurantRepository.getRestaurants() } returns restaurantsResponse

        // Act
        val fetchedRestaurants = fetchRestaurantsUseCase()

        // Assert
        assert(fetchedRestaurants.size == 3)
        assert(fetchedRestaurants[0].name == restaurantWithBestDeal.name)
    }

    @Test
    fun `Empty restaurant list`() = runTest {
        // Arrange
        val restaurantsResponse = Restaurants(
            emptyList()
        )

        coEvery { restaurantRepository.getRestaurants() } returns restaurantsResponse

        // Act
        val fetchedRestaurants = fetchRestaurantsUseCase()

        // Assert
        assert(fetchedRestaurants.isEmpty())
    }

    @Test
    fun `Null restaurant list`() = runTest {
        // Arrange
        val restaurantsResponse = Restaurants(
            null
        )

        coEvery { restaurantRepository.getRestaurants() } returns restaurantsResponse

        // Act
        val fetchedRestaurants = fetchRestaurantsUseCase()

        // Assert
        assert(fetchedRestaurants.isEmpty())
    }

    @Test
    fun `Restaurants with empty deals`() = runTest {
        // Arrange
        val restaurantsResponse = Restaurants(
            listOf(
                restaurantWithEmptyDeal,
                restaurantWithGoodDeal,
            )
        )

        coEvery { restaurantRepository.getRestaurants() } returns restaurantsResponse

        // Act
        val fetchedRestaurants = fetchRestaurantsUseCase()

        // Assert
        assertEquals(restaurantWithGoodDeal, fetchedRestaurants[0])
        assertEquals(restaurantWithEmptyDeal, fetchedRestaurants[1])
    }

    @Test
    fun `Restaurants with null deals`() = runTest {
        // Arrange
        val restaurantsResponse = Restaurants(
            listOf(
                restaurantWithNullDeal,
                restaurantWithGoodDeal,
            )
        )

        coEvery { restaurantRepository.getRestaurants() } returns restaurantsResponse

        // Act
        val fetchedRestaurants = fetchRestaurantsUseCase()

        // Assert
        assertEquals(restaurantWithGoodDeal, fetchedRestaurants[0])
        assertEquals(restaurantWithNullDeal, fetchedRestaurants[1])
    }

    @Test
    fun `Restaurants with multiple deals`() = runTest {
        // Arrange
        val restaurantsResponse = Restaurants(
            listOf(
                restaurantWithMultipleUnsortedDeals,
                restaurantWithBestDeal,
            )
        )

        coEvery { restaurantRepository.getRestaurants() } returns restaurantsResponse

        // Act
        val fetchedRestaurants = fetchRestaurantsUseCase()

        // Assert
        val expectedMultipleDeals = restaurantWithMultipleUnsortedDeals.copy(
            deals = listOf(
                Deal(discount = 50),
                Deal(discount = 25),
                Deal(discount = 0)
            )
        )
        assertEquals(restaurantWithBestDeal, fetchedRestaurants[0])
        assertEquals(expectedMultipleDeals, fetchedRestaurants[1])
    }

    @Test
    fun `Multiple restaurants with same highest discounts`() = runTest {
        // Arrange
        val restaurantsResponse = Restaurants(
            listOf(
                restaurantWithHighestDealLowerSecondDeal,
                restaurantWithBestDeal,
            )
        )

        coEvery { restaurantRepository.getRestaurants() } returns restaurantsResponse

        // Act
        val fetchedRestaurants = fetchRestaurantsUseCase()

        // Assert
        val expectedMultipleDeals = restaurantWithHighestDealLowerSecondDeal.copy(
            deals = listOf(
                Deal(discount = 60),
                Deal(discount = 20),
            )
        )
        assertEquals(restaurantWithBestDeal, fetchedRestaurants[0])
        assertEquals(expectedMultipleDeals, fetchedRestaurants[1])
    }

    @Test
    fun `Single restaurant with single deal`() = runTest {
        // Arrange
        val restaurantsResponse = Restaurants(
            listOf(
                restaurantWithBestDeal,
            )
        )

        coEvery { restaurantRepository.getRestaurants() } returns restaurantsResponse

        // Act
        val fetchedRestaurants = fetchRestaurantsUseCase()

        // Assert
        assertEquals(restaurantWithBestDeal, fetchedRestaurants[0])
    }
}
