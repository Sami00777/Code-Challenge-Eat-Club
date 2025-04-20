package com.eatclub.challenge.data.repository

import android.util.Log
import com.eatclub.challenge.data.network.ApiService
import com.eatclub.challenge.data.api.model.Restaurants
import com.eatclub.challenge.domain.repository.RestaurantRepository
import java.io.IOException
import javax.inject.Inject

class RestaurantRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : RestaurantRepository {
    override suspend fun getRestaurants(): Restaurants? {
        try {
            val response = apiService.getRestaurants()
            return response
        } catch (e: IOException) {
            Log.e(this::class.simpleName, "Network error: ${e.message}")
            return null
        }
    }
}
