package org.mozilla.social.feed

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import org.mozilla.social.core.data.repository.FeedRepository
import org.mozilla.social.model.Page
import org.mozilla.social.model.Status

/**
 * Produces a flow of pages of statuses for a feed
 */
class FeedViewModel(feedRepository: FeedRepository) : ViewModel() {
    val feed: Flow<Page<List<Status>>> = feedRepository.getLocalTimeline().filterNotNull()
}