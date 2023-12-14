package org.mozilla.social.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.repository.mastodon.TimelineRepository
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.toPostCardUiState
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.feed.remoteMediators.FederatedTimelineRemoteMediator
import org.mozilla.social.feed.remoteMediators.HomeTimelineRemoteMediator
import org.mozilla.social.feed.remoteMediators.LocalTimelineRemoteMediator

/**
 * Produces a flow of pages of statuses for a feed
 */
class FeedViewModel(
    private val analytics: Analytics,
    private val navigateTo: NavigateTo,
    homeTimelineRemoteMediator: HomeTimelineRemoteMediator,
    localTimelineRemoteMediator: LocalTimelineRemoteMediator,
    federatedTimelineRemoteMediator: FederatedTimelineRemoteMediator,
    timelineRepository: TimelineRepository,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), FeedInteractions {
    private val userAccountId: String = getLoggedInUserAccountId()

    private val _timelineType = MutableStateFlow(TimelineType.FOR_YOU)
    val timelineType = _timelineType.asStateFlow()

    @OptIn(ExperimentalPagingApi::class)
    val homeFeed = timelineRepository.getHomeTimelinePager(
        remoteMediator = homeTimelineRemoteMediator,
    ).map { pagingData ->
        pagingData.map {
            it.toPostCardUiState(userAccountId)
        }
    }.cachedIn(viewModelScope)

    @OptIn(ExperimentalPagingApi::class)
    val localFeed = timelineRepository.getLocalTimelinePager(
        remoteMediator = localTimelineRemoteMediator,
    ).map { pagingData ->
        pagingData.map {
            it.toPostCardUiState(userAccountId)
        }
    }.cachedIn(viewModelScope)

    @OptIn(ExperimentalPagingApi::class)
    val federatedFeed = timelineRepository.getFederatedTimelinePager(
        remoteMediator = federatedTimelineRemoteMediator,
    ).map { pagingData ->
        pagingData.map {
            it.toPostCardUiState(userAccountId)
        }
    }.cachedIn(viewModelScope)

    val homePostCardDelegate: PostCardDelegate by KoinJavaComponent.inject(
        PostCardDelegate::class.java,
    ) { parametersOf(viewModelScope, AnalyticsIdentifiers.FEED_PREFIX_HOME) }
    val localPostCardDelegate: PostCardDelegate by KoinJavaComponent.inject(
        PostCardDelegate::class.java,
    ) { parametersOf(viewModelScope, AnalyticsIdentifiers.FEED_PREFIX_LOCAL) }
    val federatedPostCardDelegate: PostCardDelegate by KoinJavaComponent.inject(
        PostCardDelegate::class.java,
    ) { parametersOf(viewModelScope, AnalyticsIdentifiers.FEED_PREFIX_FEDERATED) }

    override fun onTabClicked(timelineType: TimelineType) {
        _timelineType.update { timelineType }
    }

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.FEED_SCREEN_IMPRESSION,
        )
    }

    override fun onSearchClicked() {
        navigateTo(NavigationDestination.Search)
    }
}
