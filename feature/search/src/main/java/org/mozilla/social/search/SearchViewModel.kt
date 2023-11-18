package org.mozilla.social.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.repository.mastodon.TimelineRepository

class SearchViewModel(
    private val timelineRepository: TimelineRepository,
) : ViewModel() {
    val feed: Flow<List<Status>> = retrievePublicTimeline()

    private fun retrievePublicTimeline(): Flow<List<Status>> {
        return flow {}
    }
}