package social.firefly.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import social.firefly.common.utils.edit
import social.firefly.core.analytics.DiscoverAnalytics
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.mastodon.TrendsRepository
import social.firefly.core.repository.paging.TrendingHashtagsRemoteMediator
import social.firefly.core.repository.paging.TrendingStatusPagingDataFlow
import social.firefly.core.ui.common.following.FollowStatus
import social.firefly.core.ui.common.hashtag.quickview.toHashTagQuickViewUiState
import social.firefly.core.ui.postcard.PostCardDelegate
import social.firefly.core.ui.postcard.PostCardInteractions
import social.firefly.core.ui.postcard.toPostCardUiState
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId
import social.firefly.core.usecase.mastodon.hashtag.FollowHashTag
import social.firefly.core.usecase.mastodon.hashtag.UnfollowHashTag
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class DiscoverViewModel(
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    trendsRepository: TrendsRepository,
    trendingStatusPagingDataFlow: TrendingStatusPagingDataFlow,
    private val analytics: DiscoverAnalytics,
    private val navigateTo: NavigateTo,
    private val followHashTag: FollowHashTag,
    private val unfollowHashTag: UnfollowHashTag,
    hashtagsRemoteMediator: TrendingHashtagsRemoteMediator,
    postCardDelegate: PostCardDelegate,
) : ViewModel(), DiscoverInteractions, PostCardInteractions by postCardDelegate {

    private val usersAccountId: String = getLoggedInUserAccountId()

    private val hashtags: DiscoverTab.Hashtags =
        DiscoverTab.Hashtags(pagingDataFlow = trendsRepository.getPager(
            remoteMediator = hashtagsRemoteMediator
        ).map { pagingData -> pagingData.map { hashtag -> hashtag.toHashTagQuickViewUiState() } })

    private val posts: DiscoverTab.Posts =
        DiscoverTab.Posts(trendingStatusPagingDataFlow.pagingDataFlow()
            .map { pagingData ->
                pagingData.map { hashtag ->
                    hashtag.toPostCardUiState(
                        currentUserAccountId = usersAccountId,
                        postCardInteractions = this,
                    )
                }
            })

    private val _uiState = MutableStateFlow(
        DiscoverUiState(
            selectedTab = hashtags,
            tabs = listOf(hashtags, posts)
        )
    )
    val uiState = _uiState.asStateFlow()

    override fun onRetryClicked() {
        // TODO@DA
    }

    override fun onRecommendationClicked(recommendationId: String) {
        analytics.recommendationOpened(recommendationId)
    }

    override fun onShareClicked(recommendationId: String) {
        analytics.recommendationShared(recommendationId)
    }

    override fun onScreenViewed() {
        analytics.discoverScreenViewed()
    }

    override fun onSearchBarClicked() {
        navigateTo(NavigationDestination.Search)
    }

    override fun onTabClicked(tab: DiscoverTab) {
        _uiState.edit {
            copy(
                selectedTab = tab
            )
        }
    }

    override fun onHashTagFollowClicked(name: String, followStatus: FollowStatus) {
        viewModelScope.launch {
            when (followStatus) {
                FollowStatus.FOLLOWING,
                FollowStatus.PENDING_REQUEST -> {
                    try {
                        unfollowHashTag(name)
                    } catch (e: UnfollowHashTag.UnfollowFailedException) {
                        Timber.e(e)
                    }
                }

                FollowStatus.NOT_FOLLOWING -> {
                    try {
                        followHashTag(name)
                    } catch (e: FollowHashTag.FollowFailedException) {
                        Timber.e(e)
                    }
                }
            }
        }
    }

    override fun onHashtagClick(name: String) {
        navigateTo(NavigationDestination.HashTag(name))
    }
}
