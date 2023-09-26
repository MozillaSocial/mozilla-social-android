package org.mozilla.social.common

sealed class Resource<T> {

    class Loading<T> : Resource<T>()

    data class Loaded<T>(
        val data: T,
    ) : Resource<T>()

    data class Error<T>(
        val exception: Exception,
    ) : Resource<T>()
}