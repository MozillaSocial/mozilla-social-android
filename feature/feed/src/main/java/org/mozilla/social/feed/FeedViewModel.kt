package org.mozilla.social.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.data.repository.TimelineRepository
import org.mozilla.social.core.domain.HomeTimelinePagingSource
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.core.ui.postcard.toPostCardUiState


/**
 * Produces a flow of pages of statuses for a feed
 */
class FeedViewModel(
    private val timelineRepository: TimelineRepository,
    private val accountRepository: AccountRepository,
    private val statusRepository: StatusRepository,
    private val log: Log,
    private val onReplyClicked: (String) -> Unit,
) : ViewModel(), PostCardInteractions {

    private lateinit var currentSource: HomeTimelinePagingSource

    val feed = Pager(
        PagingConfig(
            pageSize = 20,
            initialLoadSize = 40
        )
    ) {
        currentSource = HomeTimelinePagingSource(
            timelineRepository = timelineRepository,
            accountRepository = accountRepository,
        )
        currentSource
    }.flow.map { pagingData ->
        pagingData.map {
            it.toPostCardUiState()
        }
    }.cachedIn(viewModelScope)

    private val currentFeedType = MutableStateFlow(INITIAL_FEED).also {
        viewModelScope.launch {
            it.collect { feedType ->
                when (feedType) {
                    FeedType.HOME -> {}
                    FeedType.LOCAL -> {}
                }
            }
        }
    }

    override fun onVoteClicked(pollId: String, choices: List<Int>) {
        viewModelScope.launch {
            try {
                statusRepository.voteOnPoll(pollId, choices)
                currentSource.invalidate()
            } catch (e: Exception) {

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

    companion object {
        private val INITIAL_FEED = FeedType.HOME
    }
}

enum class FeedType {
    HOME, LOCAL,
}