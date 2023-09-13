package org.mozilla.social.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.RecommendationRepository
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.model.statusCollections.toStatusWrapper
import org.mozilla.social.core.domain.AccountIdFlow
import org.mozilla.social.core.domain.HomeTimelineRemoteMediator
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.toPostCardUiState

/**
 * Produces a flow of pages of statuses for a feed
 */
class FeedViewModel(
    homeTimelineRemoteMediator: HomeTimelineRemoteMediator,
    accountIdFlow: AccountIdFlow,
    statusRepository: StatusRepository,
    recommendationRepository: RecommendationRepository,
    accountRepository: AccountRepository,
    private val socialDatabase: SocialDatabase,
    log: Log,
    onPostClicked: (String) -> Unit,
    onReplyClicked: (String) -> Unit,
    onReportClicked: (accountId: String, statusId: String) -> Unit,
) : ViewModel() {

    private val currentUserAccountId: StateFlow<String> =
        accountIdFlow().filterNotNull()
            .stateIn(
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

    val reccs = flow {
        try {
            emit(recommendationRepository.getRecommendations())
        } catch (exception: Exception) {
        }
    }

    val postCardDelegate = PostCardDelegate(
        coroutineScope = viewModelScope,
        statusRepository = statusRepository,
        accountRepository = accountRepository,
        log = log,
        onReplyClicked = onReplyClicked,
        onPostClicked = onPostClicked,
        onReportClicked = onReportClicked,
    )

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

    companion object {
        private val INITIAL_FEED = FeedType.HOME
    }
}

enum class FeedType {
    HOME, LOCAL,
}