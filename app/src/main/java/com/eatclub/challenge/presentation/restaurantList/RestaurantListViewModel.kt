package com.eatclub.challenge.presentation.restaurantList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eatclub.challenge.data.api.model.Restaurant
import com.eatclub.challenge.di.model.CoroutineDispatchers
import com.eatclub.challenge.data.network.NetworkMonitor
import com.eatclub.challenge.domain.common.state.ErrorType
import com.eatclub.challenge.domain.common.state.NetworkRequestState
import com.eatclub.challenge.domain.repository.FetchRestaurantsUseCase
import com.eatclub.challenge.domain.repository.FilterRestaurantsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RestaurantListViewModel @Inject constructor(
    private val fetchRestaurantsUseCase: FetchRestaurantsUseCase,
    private val filterRestaurantsUseCase: FilterRestaurantsUseCase,
    private val dispatchers: CoroutineDispatchers,
    private val networkMonitor: NetworkMonitor,
) : ViewModel() {
    private var allRestaurants: List<Restaurant> = emptyList()

    private val _filteredRestaurants = MutableLiveData<NetworkRequestState<List<Restaurant>>>()
    val filteredRestaurants: LiveData<NetworkRequestState<List<Restaurant>>> = _filteredRestaurants

    init {
        getRestaurantList()
    }

    fun getRestaurantList() {
        if (!networkMonitor.isNetworkAvailable()) {
            _filteredRestaurants.value =
                NetworkRequestState.Error(ErrorType.UnavailableNetwork("No Internet Connection"))
            return
        }
        _filteredRestaurants.value = NetworkRequestState.Loading
        viewModelScope.launch(dispatchers.io) {
            try {
                val response = fetchRestaurantsUseCase()
                withContext(dispatchers.main) {
                    if (response.isNotEmpty()) {
                        allRestaurants = response
                        _filteredRestaurants.value = NetworkRequestState.Success(response)
                    } else {
                        _filteredRestaurants.value =
                            NetworkRequestState.Error(ErrorType.EmptyResult("No Result Found"))
                    }
                }
            } catch (e: IOException) {
                _filteredRestaurants.postValue(NetworkRequestState.Error(ErrorType.NetworkError(e)))
            } catch (e: Exception) {
                _filteredRestaurants.postValue(NetworkRequestState.Error(ErrorType.GeneralError(e)))
            }
        }
    }

    fun filterRestaurants(query: String) {
        viewModelScope.launch(dispatchers.default) {
            val result = filterRestaurantsUseCase(query, allRestaurants)
            withContext(dispatchers.main) {
                if (result.isEmpty()) {
                    _filteredRestaurants.value =
                        NetworkRequestState.Error(ErrorType.EmptyResult("No Result Found for $query"))
                } else {
                    _filteredRestaurants.value = NetworkRequestState.Success(result)
                }
            }
        }
    }

    fun resetFilter() {
        _filteredRestaurants.postValue(NetworkRequestState.Success(allRestaurants))
    }
}
