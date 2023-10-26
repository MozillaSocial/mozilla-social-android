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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.model.statusCollections.toStatusWrapper
import org.mozilla.social.core.domain.AccountIdFlow
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.PostCardNavigation
import org.mozilla.social.core.ui.postcard.toPostCardUiState

/**
 * Produces a flow of pages of statuses for a feed
 */
class FeedViewModel(
    accountIdFlow: AccountIdFlow,
    statusRepository: StatusRepository,
    accountRepository: AccountRepository,
    private val socialDatabase: SocialDatabase,
    log: Log,
    postCardNavigation: PostCardNavigation,
) : ViewModel(), FeedInteractions {

    private val _timelineType = MutableStateFlow(TimelineType.FOR_YOU)
    val timelineType = _timelineType.asStateFlow()

    private val homeTimelineRemoteMediator: HomeTimelineRemoteMediator by KoinJavaComponent.inject(
        HomeTimelineRemoteMediator::class.java
    ) { parametersOf(
        timelineType,
    ) }

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
        remoteMediator = homeTimelineRemoteMediator,
    ) {
        socialDatabase.homeTimelineDao().homeTimelinePagingSource()
    }.flow.map { pagingData ->
        pagingData.map {
            it.toStatusWrapper().toExternalModel().toPostCardUiState(currentUserAccountId.value)
        }
    }.cachedIn(viewModelScope)

    val postCardDelegate = PostCardDelegate(
        coroutineScope = viewModelScope,
        statusRepository = statusRepository,
        accountRepository = accountRepository,
        log = log,
        postCardNavigation = postCardNavigation,
    )

    override fun onTabClicked(timelineType: TimelineType) {
        _timelineType.update { timelineType }
    }
}
