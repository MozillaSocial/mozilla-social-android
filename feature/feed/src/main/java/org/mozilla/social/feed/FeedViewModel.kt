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
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.model.statusCollections.toStatusWrapper
import org.mozilla.social.core.domain.AccountIdFlow
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.toPostCardUiState
import org.mozilla.social.feed.remoteMediators.FederatedTimelineRemoteMediator
import org.mozilla.social.feed.remoteMediators.HomeTimelineRemoteMediator
import org.mozilla.social.feed.remoteMediators.LocalTimelineRemoteMediator

/**
 * Produces a flow of pages of statuses for a feed
 */
class FeedViewModel(
    private val analytics: Analytics,
    homeTimelineRemoteMediator: HomeTimelineRemoteMediator,
    localTimelineRemoteMediator: LocalTimelineRemoteMediator,
    federatedTimelineRemoteMediator: FederatedTimelineRemoteMediator,
    accountIdFlow: AccountIdFlow,
    private val socialDatabase: SocialDatabase,
) : ViewModel(), FeedInteractions {

    private val _timelineType = MutableStateFlow(TimelineType.FOR_YOU)
    val timelineType = _timelineType.asStateFlow()

    private val currentUserAccountId: StateFlow<String> =
        accountIdFlow().filterNotNull()
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                ""
            )

    @OptIn(ExperimentalPagingApi::class)
    val homeFeed = Pager(
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

    @OptIn(ExperimentalPagingApi::class)
    val localFeed = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40
        ),
        remoteMediator = localTimelineRemoteMediator,
    ) {
        socialDatabase.localTimelineDao().localTimelinePagingSource()
    }.flow.map { pagingData ->
        pagingData.map {
            it.toStatusWrapper().toExternalModel().toPostCardUiState(currentUserAccountId.value)
        }
    }.cachedIn(viewModelScope)

    @OptIn(ExperimentalPagingApi::class)
    val federatedFeed = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40
        ),
        remoteMediator = federatedTimelineRemoteMediator,
    ) {
        socialDatabase.federatedTimelineDao().federatedTimelinePagingSource()
    }.flow.map { pagingData ->
        pagingData.map {
            it.toStatusWrapper().toExternalModel().toPostCardUiState(currentUserAccountId.value)
        }
    }.cachedIn(viewModelScope)

    val postCardDelegate: PostCardDelegate by KoinJavaComponent.inject(
        PostCardDelegate::class.java
    ) { parametersOf(viewModelScope) }

    override fun onTabClicked(timelineType: TimelineType) {
        _timelineType.update { timelineType }
    }

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.FEED_SCREEN_IMPRESSION,
        )
    }
}
