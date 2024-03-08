package social.firefly.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import social.firefly.core.analytics.DiscoverAnalytics
import social.firefly.core.analytics.utils.ImpressionTracker
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.mastodon.DatabaseDelegate
import social.firefly.core.repository.mastodon.TrendsRepository
import social.firefly.core.repository.paging.TrendingHashtagsRemoteMediator
import social.firefly.core.ui.common.following.FollowStatus
import social.firefly.core.ui.common.hashtag.quickview.HashTagQuickViewUiState
import social.firefly.core.ui.common.hashtag.quickview.toHashTagQuickViewUiState
import social.firefly.core.usecase.mastodon.hashtag.FollowHashTag
import social.firefly.core.usecase.mastodon.hashtag.UnfollowHashTag
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class DiscoverViewModel(
    trendsRepository: TrendsRepository,
    private val analytics: DiscoverAnalytics,
    private val navigateTo: NavigateTo,
    private val followHashTag: FollowHashTag,
    private val unfollowHashTag: UnfollowHashTag,
    remoteMediator: TrendingHashtagsRemoteMediator,
) : ViewModel(), DiscoverInteractions {
    @OptIn(ExperimentalPagingApi::class)
     val trendingHashtags: Flow<PagingData<HashTagQuickViewUiState>> = trendsRepository.getHashTagsPager(
        remoteMediator = remoteMediator
     ).map { it.map { it.toHashTagQuickViewUiState() } }

    private val recommendationImpressionTracker =
        ImpressionTracker<String> { recommendationId ->
            analytics.recommendationViewed(recommendationId)
        }

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

    override fun onRecommendationViewed(recommendationId: String) {
        recommendationImpressionTracker.track(recommendationId)
    }

    override fun onSearchBarClicked() {
        navigateTo(NavigationDestination.Search)
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
