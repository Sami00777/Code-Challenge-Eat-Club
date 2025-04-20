package com.eatclub.challenge.domain.usecase

import com.eatclub.challenge.data.api.model.Restaurant
import com.eatclub.challenge.domain.repository.FilterRestaurantsUseCase
import javax.inject.Inject

class FilterRestaurantsUseCaseImpl @Inject constructor() : FilterRestaurantsUseCase {
    /**
     * Filters the list of restaurants based on the provided query filter by partial name, and cuisines.
     *
     * @param query The search query to filter the restaurants.
     * @param restaurants The list of restaurants to be filtered.
     * @return A list of restaurants that match the search query.
     */
    override suspend fun invoke(
        query: String,
        restaurants: List<Restaurant>,
    ): List<Restaurant> {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty() || trimmedQuery.length < 2) return restaurants
        return restaurants.filter { restaurant ->
            val nameMatch = restaurant.name?.contains(trimmedQuery, true) == true
            val cuisineMatch =
                restaurant.cuisines?.any { it.contains(trimmedQuery, true) == true } == true
            nameMatch || cuisineMatch
        }
    }
}
