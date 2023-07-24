package org.mozilla.social.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import org.mozilla.social.core.data.repository.FeedRepository
import org.mozilla.social.model.Page
import org.mozilla.social.model.Status

class SearchViewModel(
    private val feedRepository: FeedRepository
) : ViewModel() {
    val publicFeed: Flow<Page<List<Status>>> = feedRepository.getPublicTimeline().filterNotNull()
}