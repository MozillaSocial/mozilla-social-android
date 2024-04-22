package social.firefly.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import social.firefly.common.utils.edit
import social.firefly.core.analytics.FeedAnalytics
import social.firefly.core.analytics.FeedLocation
import social.firefly.core.datastore.UserPreferencesDatastore
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.repository.paging.FederatedTimelineRemoteMediator
import social.firefly.core.repository.paging.HomeTimelineRemoteMediator
import social.firefly.core.repository.paging.LocalTimelineRemoteMediator
import social.firefly.core.ui.postcard.PostCardDelegate
import social.firefly.core.ui.postcard.toPostCardUiState
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId

/**
 * Produces a flow of pages of statuses for a feed
 */
@OptIn(ExperimentalPagingApi::class)
class FeedViewModel(
    private val analytics: FeedAnalytics,
    private val userPreferencesDatastore: UserPreferencesDatastore,
    homeTimelineRemoteMediator: HomeTimelineRemoteMediator,
    localTimelineRemoteMediator: LocalTimelineRemoteMediator,
    federatedTimelineRemoteMediator: FederatedTimelineRemoteMediator,
    private val timelineRepository: TimelineRepository,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), FeedInteractions {
    private val userAccountId: String = getLoggedInUserAccountId()

    private var statusViewedJob: Job? = null
    private var homeFirstVisibleItemIndex = 0
    private var homePrependEndReached = false
    private var hasBeenToTopOfHome = false

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState = _uiState.asStateFlow()

    @OptIn(ExperimentalPagingApi::class)
    val homeFeed = timelineRepository.getHomeTimelinePager(
        remoteMediator = homeTimelineRemoteMediator,
    ).map { pagingData ->
        pagingData.map {
            it.toPostCardUiState(userAccountId, homePostCardDelegate)
        }
    }.cachedIn(viewModelScope)

    @OptIn(ExperimentalPagingApi::class)
    val localFeed = timelineRepository.getLocalTimelinePager(
        remoteMediator = localTimelineRemoteMediator,
    ).map { pagingData ->
        pagingData.map {
            it.toPostCardUiState(userAccountId, localPostCardDelegate)
        }
    }.cachedIn(viewModelScope)

    @OptIn(ExperimentalPagingApi::class)
    val federatedFeed = timelineRepository.getFederatedTimelinePager(
        remoteMediator = federatedTimelineRemoteMediator,
    ).map { pagingData ->
        pagingData.map {
            it.toPostCardUiState(userAccountId, federatedPostCardDelegate)
        }
    }.cachedIn(viewModelScope)

    val homePostCardDelegate: PostCardDelegate by KoinJavaComponent.inject(
        PostCardDelegate::class.java,
    ) { parametersOf(FeedLocation.HOME) }
    val localPostCardDelegate: PostCardDelegate by KoinJavaComponent.inject(
        PostCardDelegate::class.java,
    ) { parametersOf(FeedLocation.LOCAL) }
    val federatedPostCardDelegate: PostCardDelegate by KoinJavaComponent.inject(
        PostCardDelegate::class.java,
    ) { parametersOf(FeedLocation.FEDERATED) }

    /**
     * We restore the user's place in their timeline by removing items in the database
     * above their last seen item.  This needs to happen before we start observing the
     * home timeline.
     */

    override fun onTabClicked(timelineType: TimelineType) {
        analytics.feedScreenClicked(timelineType.toAnalyticsTimelineType())
        _uiState.edit { copy(
            timelineType = timelineType
        ) }
    }

    override fun onScreenViewed() {
        analytics.feedScreenViewed()
    }

    @Suppress("MagicNumber")
    override suspend fun onScrollToTopClicked(onDatabaseCleared: suspend () -> Unit) {
        timelineRepository.deleteHomeTimeline()
        // race condition where the database needs to emit it's changes
        delay(200)
        onDatabaseCleared()
    }

    override fun onFirstVisibleItemIndexForHomeChanged(
        index: Int,
        statusId: String,
    ) {
        // save the last seen status no more than once per x seconds (SAVE_RATE)
        if (statusViewedJob == null) {
            statusViewedJob = viewModelScope.launch {
                userPreferencesDatastore.saveLastSeenHomeStatusId(statusId)
                delay(SAVE_RATE)
                statusViewedJob = null
            }
        }
        homeFirstVisibleItemIndex = index
        updateScrollUpButtonVisibility()
    }

    override fun onHomePrependEndReached(reached: Boolean) {
        homePrependEndReached = reached
        updateScrollUpButtonVisibility()
    }

    private fun updateScrollUpButtonVisibility() {
        if (homePrependEndReached &&
            homeFirstVisibleItemIndex <= 1) {
            hasBeenToTopOfHome = true
        }
        val visible = !hasBeenToTopOfHome

        _uiState.edit { copy(
            scrollUpButtonVisible = visible,
        ) }
    }

    companion object {
        private const val SAVE_RATE = 1_500L
    }
}

private fun TimelineType.toAnalyticsTimelineType(): FeedAnalytics.TimelineType {
    return when (this) {
        TimelineType.FOR_YOU -> FeedAnalytics.TimelineType.FOR_YOU
        TimelineType.LOCAL -> FeedAnalytics.TimelineType.LOCAL
        TimelineType.FEDERATED -> FeedAnalytics.TimelineType.FEDERATED
    }
}
