package com.eatclub.challenge.domain.repository

import com.eatclub.challenge.data.api.model.Restaurant

interface FetchRestaurantsUseCase {
    suspend operator fun invoke(): List<Restaurant>
}
