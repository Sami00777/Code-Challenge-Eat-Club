package com.eatclub.challenge.domain.usecase

import com.eatclub.challenge.data.api.model.Restaurant
import com.eatclub.challenge.domain.repository.FetchRestaurantsUseCase
import com.eatclub.challenge.domain.repository.RestaurantRepository
import javax.inject.Inject

class FetchRestaurantsUseCaseImpl @Inject constructor(
    private val restaurantRepository: RestaurantRepository,
) : FetchRestaurantsUseCase {
    /**
     * Fetches a list of restaurants from the repository and returns them sorted by the strength of their deals.
     *
     * Each restaurant's deals are first sorted internally in descending order of discount value.
     * Then, the list of restaurants is sorted based on:
     *   1. The highest available discount from its deals (descending).
     *   2. If tied, the second-highest discount is used as a tiebreaker (also descending).
     *
     * Restaurants or deals with null discount values are handled gracefully and excluded from comparison.
     *
     * @return A list of [Restaurant] objects prioritized by their strongest available deals.
     */
    override suspend fun invoke(): List<Restaurant> {
        val response = restaurantRepository.getRestaurants()
        val restaurants = response?.restaurants.orEmpty().map { restaurant ->
            restaurant.copy(
                deals = restaurant.deals?.sortedByDescending { deal ->
                    deal.discount
                },
            )
        }
        return restaurants.sortedWith(
            compareByDescending<Restaurant> { restaurant ->
                restaurant.deals
                    ?.mapNotNull { it.discount }
                    ?.maxOrNull() ?: 0
            }.thenByDescending { restaurant ->
                restaurant.deals
                    ?.mapNotNull { it.discount }
                    ?.sorted()
                    ?.reversed()
                    ?.getOrNull(1) ?: 0
            },
        )
    }
}
