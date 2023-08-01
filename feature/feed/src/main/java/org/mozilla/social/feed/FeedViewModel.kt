package org.mozilla.social.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.data.repository.FeedRepository
import org.mozilla.social.model.Status

/**
 * Produces a flow of pages of statuses for a feed
 */
class FeedViewModel(
    private val feedRepository: FeedRepository,
    private val log: Log
) : ViewModel() {

    private val _statusFeed = MutableStateFlow<List<Status>>(emptyList())
    val statusFeed = _statusFeed.asStateFlow()

    private val currentFeedType = MutableStateFlow(INITIAL_FEED).also {
        viewModelScope.launch {
            it.collect { feedType ->
                when (feedType) {
                    FeedType.HOME -> getHomeTimeline()
                    FeedType.LOCAL -> {}
                }
            }
        }
    }

    fun showHomeTimeline() {
        currentFeedType.value = FeedType.HOME
    }

    fun showLocalTimeline() {
        currentFeedType.value = FeedType.LOCAL
    }

    private fun getHomeTimeline() {
        viewModelScope.launch {
            try {
                _statusFeed.update { feedRepository.retrieveHomeTimeline() }
            } catch (e: Exception) {
                log.e(e)
            }
        }
    }

    companion object {
        private val INITIAL_FEED = FeedType.HOME
    }
}

enum class FeedType {
    HOME, LOCAL,
}