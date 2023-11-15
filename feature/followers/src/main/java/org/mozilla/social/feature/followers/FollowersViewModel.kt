package org.mozilla.social.feature.followers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.map
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.repository.mastodon.FollowersRepository
import org.mozilla.social.core.repository.mastodon.FollowingsRepository
import org.mozilla.social.core.ui.common.account.quickview.toQuickViewUiState
import org.mozilla.social.core.usecase.mastodon.remotemediators.FollowersRemoteMediator
import org.mozilla.social.core.usecase.mastodon.remotemediators.FollowingsRemoteMediator

@OptIn(ExperimentalPagingApi::class)
class FollowersViewModel(
    private val accountId: String,
    private val followersRepository: FollowersRepository,
    private val followingsRepository: FollowingsRepository,
    private val navigateTo: NavigateTo,
    private val analytics: Analytics,
) : ViewModel(), FollowersInteractions {

    private val followersRemoteMediator: FollowersRemoteMediator by KoinJavaComponent.inject(
        FollowersRemoteMediator::class.java
    ) { parametersOf(accountId) }

    private val followingsRemoteMediator: FollowingsRemoteMediator by KoinJavaComponent.inject(
        FollowingsRemoteMediator::class.java
    ) { parametersOf(accountId) }

    val followers = Pager(
        PagingConfig(
            pageSize = 40,
            initialLoadSize = 40,
        ),
        remoteMediator = followersRemoteMediator,
    ) {
        followersRepository.getFollowersPagingSource(accountId = accountId)
    }.flow.map { pagingData ->
        pagingData.map {
            it.toQuickViewUiState()
        }
    }.cachedIn(viewModelScope)

    val following = Pager(
        PagingConfig(
            pageSize = 40,
            initialLoadSize = 40,
        ),
        remoteMediator = followingsRemoteMediator
    ) {
        followingsRepository.getFollowingsPagingSource(accountId = accountId)
    }.flow.map { pagingData ->
        pagingData.map {
            it.toQuickViewUiState()
        }
    }.cachedIn(viewModelScope)

    override fun onAccountClicked(accountId: String) {
        navigateTo(NavigationDestination.Account(accountId))
    }

    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.FOLLOWERS_SCREEN_IMPRESSION,
        )
    }
}