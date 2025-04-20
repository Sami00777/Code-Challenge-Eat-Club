package com.eatclub.challenge.domain.common.state

import java.io.IOException

sealed class NetworkRequestState<out T> {
    data object Loading : NetworkRequestState<Nothing>()

    data class Success<out T>(val data: T) : NetworkRequestState<T>()

    data class Error(val error: ErrorType) : NetworkRequestState<Nothing>()
}

sealed class ErrorType {
    data class UnavailableNetwork(val message: String) : ErrorType()

    data class NetworkError(val exception: IOException) : ErrorType()

    data class GeneralError(val exception: Exception) : ErrorType()

    data class EmptyResult(val message: String? = null) : ErrorType()
}
