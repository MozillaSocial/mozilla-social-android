package org.mozilla.social.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import timber.log.Timber

/**
 * This class is used to represent the loading state of any resource while wrapping
 * the loaded data or exception.
 *
 * Example use case:
 * You are loading a String from network.  Your view model has a Flow<Resource<String>> variable.
 * The flow emits a [Loading] while the string is load.  It emits a [Loaded] when the string is
 * loaded, or an [Error] if an exception was caught.
 * You can observe this flow in your UI and show the proper load/loaded/error state depending
 * on the [Resource] type.
 */
sealed class Resource<T> {

    class Loading<T> : Resource<T>()

    data class Loaded<T>(
        val data: T,
    ) : Resource<T>()

    data class Error<T>(
        val exception: Exception,
    ) : Resource<T>()
}

fun <T> MutableStateFlow<Resource<T>>.updateData(block: T.() -> T) {
    with(value as? Resource.Loaded ?: return) {
        update {
            Resource.Loaded(
                data = data.block()
            )
        }
    }
}

fun <T> loadResource(block: suspend () -> T) = flow {
    this.emit(Resource.Loading())
    try {
        this.emit(Resource.Loaded(block()))
    } catch (e: Exception) {
        Timber.e(e)
        this.emit(Resource.Error(e))
    }
}