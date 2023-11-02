package org.mozilla.social.feature.followers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.map
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.navigation.NavDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.navigation.usecases.PopNavBackstack
import org.mozilla.social.core.ui.account.quickview.toQuickViewUiState

class FollowersViewModel(
    private val accountId: String,
    private val followerScreenType: FollowerScreenType,
    private val accountRepository: AccountRepository,
    private val navigateTo: NavigateTo,
    private val analytics: Analytics,
) : ViewModel(), FollowersInteractions {

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
        navigateTo(NavDestination.Account(accountId))
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