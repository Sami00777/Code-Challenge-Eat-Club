package com.eatclub.challenge.domain.repository

import com.eatclub.challenge.data.api.model.Restaurants

interface RestaurantRepository {
    suspend fun getRestaurants(): Restaurants?
}
