package com.eatclub.challenge.presentation.restaurantDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatclub.challenge.data.api.model.Restaurant
import com.eatclub.challenge.di.model.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailViewModel @Inject constructor(
    private val saveStateHandle: SavedStateHandle,
    private val dispatchers: CoroutineDispatchers,
) : ViewModel() {
    private val _restaurantDetails = MutableLiveData<Restaurant?>()
    val restaurantDetails: LiveData<Restaurant?> = _restaurantDetails

    init {
        loadRestaurantDetails()
    }

    private fun loadRestaurantDetails() {
        viewModelScope.launch(dispatchers.default) {
            val restaurant = saveStateHandle.get<Restaurant>(RESTAURANT_KEY)
            withContext(dispatchers.main) {
                _restaurantDetails.value = restaurant
            }
        }
    }

    companion object {
        const val RESTAURANT_KEY = "restaurant"
    }
}
