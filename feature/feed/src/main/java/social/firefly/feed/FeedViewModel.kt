package social.firefly.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import social.firefly.common.appscope.AppScope
import social.firefly.core.analytics.FeedAnalytics
import social.firefly.core.analytics.FeedLocation
import social.firefly.core.datastore.UserPreferencesDatastore
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.repository.paging.FederatedTimelineRemoteMediator
import social.firefly.core.repository.paging.HomeTimelineRemoteMediator
import social.firefly.core.repository.paging.LocalTimelineRemoteMediator
import social.firefly.core.ui.postcard.PostCardDelegate
import social.firefly.core.ui.postcard.PostCardUiState
import social.firefly.core.ui.postcard.toPostCardUiState
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId

/**
 * Produces a flow of pages of statuses for a feed
 */
@OptIn(ExperimentalPagingApi::class)
class FeedViewModel(
    private val analytics: FeedAnalytics,
    private val userPreferencesDatastore: UserPreferencesDatastore,
    private val homeTimelineRemoteMediator: HomeTimelineRemoteMediator,
    localTimelineRemoteMediator: LocalTimelineRemoteMediator,
    federatedTimelineRemoteMediator: FederatedTimelineRemoteMediator,
    private val timelineRepository: TimelineRepository,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), FeedInteractions {
    private val userAccountId: String = getLoggedInUserAccountId()

    private val _timelineType = MutableStateFlow(TimelineType.FOR_YOU)
    val timelineType = _timelineType.asStateFlow()

    private val _homeFeed = MutableStateFlow<Flow<PagingData<PostCardUiState>>>(emptyFlow())
    var homeFeed = _homeFeed.asStateFlow()

    private var statusViewedJob: Job? = null
    private var lastSeenId: String? = null

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
    ) { parametersOf(viewModelScope, FeedLocation.HOME) }
    val localPostCardDelegate: PostCardDelegate by KoinJavaComponent.inject(
        PostCardDelegate::class.java,
    ) { parametersOf(viewModelScope, FeedLocation.LOCAL) }
    val federatedPostCardDelegate: PostCardDelegate by KoinJavaComponent.inject(
        PostCardDelegate::class.java,
    ) { parametersOf(viewModelScope, FeedLocation.FEDERATED) }

    init {
        setupHomeFeed()
    }

    /**
     * We restore the user's place in their timeline by removing items in the database
     * above their last seen item.  This needs to happen before we start observing the
     * home timeline.
     */
    private fun setupHomeFeed() {
        viewModelScope.launch {
            val lastSeenId = CompletableDeferred<String>()
            launch {
                userPreferencesDatastore.lastSeenHomeStatusId.collectLatest {
                    lastSeenId.complete(it)
                    cancel()
                }
            }
            timelineRepository.deleteHomeStatusesBeforeId(lastSeenId.await())
            _homeFeed.update {
                timelineRepository.getHomeTimelinePager(
                    remoteMediator = homeTimelineRemoteMediator,
                ).map { pagingData ->
                    pagingData.map {
                        it.toPostCardUiState(userAccountId, homePostCardDelegate)
                    }
                }.cachedIn(viewModelScope)
            }
        }
    }

    override fun onTabClicked(timelineType: TimelineType) {
        analytics.feedScreenClicked(timelineType.toAnalyticsTimelineType())
        _timelineType.update { timelineType }
    }

    override fun onScreenViewed() {
        analytics.feedScreenViewed()
    }

    override fun onStatusViewed(statusId: String) {
        lastSeenId = statusId
        // save the last seen status no more than once per x seconds (SAVE_RATE)
        if (statusViewedJob == null) {
            statusViewedJob = viewModelScope.launch {
                lastSeenId?.let { userPreferencesDatastore.saveLastSeenHomeStatusId(it) }
                delay(SAVE_RATE)
                statusViewedJob = null
            }
        }
    }

    override suspend fun onScrollToTopClicked(onDatabaseCleared: suspend () -> Unit) {
        timelineRepository.deleteHomeTimeline()
        // race condition where the database needs to emit it's changes
        delay(150)
        onDatabaseCleared()
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
