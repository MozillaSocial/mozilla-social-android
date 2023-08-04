package org.mozilla.social.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mozilla.social.core.data.repository.FeedRepository
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.data.repository.TimelineRepository
import org.mozilla.social.core.domain.TimelineUseCase
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.model.Post
import org.mozilla.social.model.Status


/**
 * Produces a flow of pages of statuses for a feed
 */
class FeedViewModel(
    private val feedRepository: FeedRepository,
    private val timelineUseCase: TimelineUseCase,
    private val log: Log,
    private val onReplyClicked: (String) -> Unit,
) : ViewModel(), PostCardInteractions {

    private val _statusFeed = MutableStateFlow<List<Post>>(emptyList())
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

    override fun onReplyClicked(statusId: String) {
        onReplyClicked.invoke(statusId)
    }

    override fun onBoostClicked() {
        super.onBoostClicked()
    }

    override fun onFavoriteClicked() {
        super.onFavoriteClicked()
    }

    override fun onShareClicked() {
        super.onShareClicked()
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
                _statusFeed.update { timelineUseCase.getHomeTimeline() }
            } catch (e: Exception) {
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