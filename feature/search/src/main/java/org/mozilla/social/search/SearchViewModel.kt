package org.mozilla.social.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mozilla.social.core.data.repository.FeedRepository
import org.mozilla.social.model.entity.Status

class SearchViewModel(
    private val feedRepository: FeedRepository
) : ViewModel() {

    val feed: Flow<List<Status>> = retrievePublicTimeline()

    private fun retrievePublicTimeline(): Flow<List<Status>> {
        return flow {}
    }
}