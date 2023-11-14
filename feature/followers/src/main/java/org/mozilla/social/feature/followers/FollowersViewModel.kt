package org.mozilla.social.feature.followers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.ui.common.account.quickview.toQuickViewUiState

class FollowersViewModel(
    private val accountId: String,
    private val followerScreenType: FollowerScreenType,
    private val accountRepository: AccountRepository,
    private val navigateTo: NavigateTo,
    private val analytics: Analytics,
) : ViewModel(), FollowersInteractions {

    private val _selectedTab = MutableStateFlow(FollowerScreenType.FOLLOWERS)
    val selectedTab = _selectedTab.asStateFlow()

    val followers = Pager(
        PagingConfig(
            pageSize = 40,
            initialLoadSize = 40,
        )
    ) {
        FollowersPagingSource(accountRepository, accountId, followerScreenType)
    }.flow.map { pagingData ->
        pagingData.map {
            it.toQuickViewUiState()
        }
    }.cachedIn(viewModelScope)

    override fun onAccountClicked(accountId: String) {
        navigateTo(NavigationDestination.Account(accountId))
    }

    override fun onScreenViewed() {
        when (followerScreenType) {
            FollowerScreenType.FOLLOWERS -> {
                analytics.uiImpression(
                    uiIdentifier = AnalyticsIdentifiers.FOLLOWERS_SCREEN_IMPRESSION,
                )
            }
            FollowerScreenType.FOLLOWING -> {
                analytics.uiImpression(
                    uiIdentifier = AnalyticsIdentifiers.FOLLOWING_SCREEN_IMPRESSION,
                )
            }
        }
    }
}