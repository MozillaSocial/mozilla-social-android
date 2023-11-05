package org.mozilla.social.common.utils

import timber.log.Timber


suspend fun <T> tryNetworkCall(block: suspend () -> T): DataResponse<T> {
    return try {
        DataResponse.Success(block())
    } catch (exception: Exception) {
        Timber.e(t = exception, message = "Error with network call")
        DataResponse.Error(exception)
    }
}

suspend fun <T> tryDbCall(block: suspend () -> T): DataResponse<T> {
    return try {
        DataResponse.Success(block())
    } catch (exception: Exception) {
        Timber.e(t = exception, message = "Error with db call")
        DataResponse.Error(exception)
    }
}

sealed class DataResponse<T> {

    open val data: T? = null

    data class Error<T>(val exception: Exception) : DataResponse<T>()
    data class Success<T>(override val data: T) : DataResponse<T>()
}