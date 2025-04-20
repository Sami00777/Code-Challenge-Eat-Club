package com.eatclub.challenge.domain.repository

import com.eatclub.challenge.data.api.model.Restaurant

interface FilterRestaurantsUseCase {
    suspend operator fun invoke(
        query: String,
        restaurants: List<Restaurant>,
    ): List<Restaurant>
}
