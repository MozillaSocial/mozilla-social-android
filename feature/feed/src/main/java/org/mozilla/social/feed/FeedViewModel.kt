package org.mozilla.social.feed

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mozilla.social.core.data.repository.FeedRepository
import org.mozilla.social.model.entity.Status

/**
 * Produces a flow of pages of statuses for a feed
 */
class FeedViewModel(
    private val feedRepository: FeedRepository
) : ViewModel() {

    val feed: Flow<List<Status>> = retrieveHomeTimeline()

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
}