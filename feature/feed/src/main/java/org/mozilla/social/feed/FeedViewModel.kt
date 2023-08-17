package org.mozilla.social.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.model.statusCollections.toStatusWrapper
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import org.mozilla.social.core.domain.HomeTimelineRemoteMediator
import org.mozilla.social.core.ui.postcard.PostCardInteractions
import org.mozilla.social.core.ui.postcard.toPostCardUiState

/**
 * Produces a flow of pages of statuses for a feed
 */
class FeedViewModel(
    homeTimelineRemoteMediator: HomeTimelineRemoteMediator,
    userPreferencesDatastore: UserPreferencesDatastore,
    private val statusRepository: StatusRepository,
    private val socialDatabase: SocialDatabase,
    private val log: Log,
    private val onReplyClicked: (String) -> Unit,
) : ViewModel(), PostCardInteractions {

    private val currentUserAccountId: StateFlow<String> =
        userPreferencesDatastore.dataStore.data.map {
            it.accountId
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            ""
        )

    @OptIn(ExperimentalPagingApi::class)
    val feed = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40
        ),
        remoteMediator = homeTimelineRemoteMediator
    ) {
        socialDatabase.homeTimelineDao().homeTimelinePagingSource()
    }.flow.map { pagingData ->
        pagingData.map {
            it.toStatusWrapper().toExternalModel().toPostCardUiState(currentUserAccountId.value)
        }
    }.cachedIn(viewModelScope)

    private val _errorToastMessage = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val errorToastMessage = _errorToastMessage.asSharedFlow()

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
            } catch (e: Exception) {
                log.e(e)
                _errorToastMessage.emit("Error Voting")
            }
        }
    }

    override fun onReplyClicked(statusId: String) {
        onReplyClicked.invoke(statusId)
    }

    override fun onBoostClicked(statusId: String) {
        //TODO don't do this here, this is only for testing that database updates work
        // move to repository or usecase in the future
        viewModelScope.launch {
            socialDatabase.statusDao().updateBoosted(statusId, true)
        }
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