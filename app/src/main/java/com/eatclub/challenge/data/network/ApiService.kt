package com.eatclub.challenge.data.network

import com.eatclub.challenge.data.api.model.Restaurants
import retrofit2.http.GET

interface ApiService {
    @GET("misc/challengedata.json")
    suspend fun getRestaurants(): Restaurants
}