package social.firefly.feature.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import social.firefly.common.utils.edit
import social.firefly.core.analytics.DiscoverAnalytics
import social.firefly.core.analytics.FeedLocation
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.paging.pagers.TrendingHashTagPager
import social.firefly.core.repository.paging.pagers.TrendingStatusPager
import social.firefly.core.ui.hashtagcard.HashTagCardDelegate
import social.firefly.core.ui.hashtagcard.quickview.toHashTagQuickViewUiState
import social.firefly.core.ui.postcard.PostCardDelegate
import social.firefly.core.ui.postcard.toPostCardUiState
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId

@OptIn(ExperimentalPagingApi::class)
class DiscoverViewModel(
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    trendingStatusPager: TrendingStatusPager,
    private val analytics: DiscoverAnalytics,
    private val navigateTo: NavigateTo,
    trendingHashTagPager: TrendingHashTagPager,
) : ViewModel(), DiscoverInteractions, KoinComponent {

    val postCardDelegate: PostCardDelegate by inject {
        parametersOf(
            FeedLocation.PROFILE,
        )
    }

    val hashTagCardDelegate: HashTagCardDelegate by inject {
        parametersOf(viewModelScope)
    }

    private val usersAccountId: String = getLoggedInUserAccountId()

    private val hashtags: DiscoverTab.Hashtags = DiscoverTab.Hashtags(
        pagingDataFlow = trendingHashTagPager.build()
            .map { pagingData ->
                pagingData.map { hashtag ->
                    hashtag.toHashTagQuickViewUiState()
                }
            }
    )

    private val posts: DiscoverTab.Posts = DiscoverTab.Posts(
        pagingDataFlow = trendingStatusPager.build()
            .map { pagingData ->
                pagingData.map { status ->
                    status.toPostCardUiState(
                        currentUserAccountId = usersAccountId,
                    )
                }
            }
    )

    private val _uiState = MutableStateFlow(
        DiscoverUiState(
            selectedTab = hashtags,
            tabs = listOf(hashtags, posts)
        )
    )
    val uiState = _uiState.asStateFlow()

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
}
