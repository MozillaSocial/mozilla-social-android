package org.mozilla.social.feed

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import org.mozilla.social.core.data.repository.FeedRepository
import org.mozilla.social.model.entity.Status

/**
 * Produces a flow of pages of statuses for a feed
 */
class FeedViewModel(
    private val feedRepository: FeedRepository,
) : ViewModel() {

    private val _currentFeedType = MutableStateFlow(INITIAL_FEED)

    val feed: Flow<List<Status>> = _currentFeedType.flatMapLatest { feedType ->
        when (feedType) {
            FeedType.HOME -> retrieveHomeTimeline()
            FeedType.LOCAL -> retrievePublicTimeline()
        }
    }

    fun showHomeTimeline() {
        _currentFeedType.value = FeedType.HOME
    }

    fun showLocalTimeline() {
        _currentFeedType.value = FeedType.LOCAL
    }

    private fun retrieveHomeTimeline(): Flow<List<Status>> {
        return flow {
            val response = feedRepository.retrieveHomeTimeline()
            if (response.isSuccessful) {
                emit(response.body() ?: emptyList())
            } else {
                val errorMsg = response.errorBody()?.string()
                emit(emptyList())
            }
        }
    }

    private fun retrievePublicTimeline(): Flow<List<Status>> {
        return flow {
            val response = feedRepository.retrievePublicTimeline()
            if (response.isSuccessful) {
                emit(response.body() ?: emptyList())
            } else {
                val errorMsg = response.errorBody()?.string()
                emit(emptyList())
            }
        }
    }
}

private val INITIAL_FEED = FeedType.HOME

enum class FeedType {
    HOME, LOCAL,
}