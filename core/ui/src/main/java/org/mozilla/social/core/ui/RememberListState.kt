package org.mozilla.social.core.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

private val savedStates = mutableMapOf<LazyListStateKey, ListStateParameters>()

/**
 * Remembers a lazy list state, based on a given key, even after the composable is destroyed.
 * Useful for saving scroll states when navigating to other screens and back.
 *
 * @param key must be unique to the lazy list
 */
@Composable
fun rememberLazyListStateForever(
    key: LazyListStateKey,
): LazyListState {
    val state = rememberSaveable(stateSaver = LazyListState.Saver) {
        val savedState = savedStates[key]
        mutableStateOf(
            LazyListState(
                savedState?.itemIndex ?: 0,
                savedState?.scrollOffset ?: 0,
            )
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            savedStates[key] = ListStateParameters(
                state.value.firstVisibleItemIndex,
                state.value.firstVisibleItemScrollOffset,
            )
        }
    }
    return state.value
}

/**
 * keys should only be used in a single place
 */
enum class LazyListStateKey {
    FEED,
}

private data class ListStateParameters(
    val itemIndex: Int,
    val scrollOffset: Int,
)