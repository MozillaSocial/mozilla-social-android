package org.mozilla.social.feature.followers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import org.mozilla.social.core.analytics.FollowersAnalytics
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.repository.mastodon.FollowersRepository
import org.mozilla.social.core.repository.mastodon.FollowingsRepository
import org.mozilla.social.core.repository.paging.FollowersRemoteMediator
import org.mozilla.social.core.repository.paging.FollowingsRemoteMediator
import org.mozilla.social.core.ui.accountfollower.toAccountFollowerUiState
import org.mozilla.social.core.usecase.mastodon.account.FollowAccount
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.account.UnfollowAccount
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class FollowersViewModel(
    private val accountId: String,
    followersRepository: FollowersRepository,
    followingsRepository: FollowingsRepository,
    private val navigateTo: NavigateTo,
    private val analytics: FollowersAnalytics,
    private val followAccount: FollowAccount,
    private val unfollowAccount: UnfollowAccount,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), FollowersInteractions {

    private val loggedInUserAccountId = getLoggedInUserAccountId()

    private val followersRemoteMediator: FollowersRemoteMediator by KoinJavaComponent.inject(
        FollowersRemoteMediator::class.java,
    ) { parametersOf(accountId) }

    private val followingsRemoteMediator: FollowingsRemoteMediator by KoinJavaComponent.inject(
        FollowingsRemoteMediator::class.java,
    ) { parametersOf(accountId) }

    val followers =
        followersRepository.getFollowersPager(
            accountId = accountId,
            remoteMediator = followersRemoteMediator,
        ).map { pagingData ->
            pagingData.map {
                it.toAccountFollowerUiState(loggedInUserAccountId)
            }
        }.cachedIn(viewModelScope)

    val following =
        followingsRepository.getFollowingsPager(
            accountId = accountId,
            remoteMediator = followingsRemoteMediator,
        ).map { pagingData ->
            pagingData.map {
                it.toAccountFollowerUiState(loggedInUserAccountId)
            }
        }.cachedIn(viewModelScope)

    override fun onAccountClicked(accountId: String) {
        navigateTo(NavigationDestination.Account(accountId))
    }

    override fun onFollowClicked(accountId: String, isFollowing: Boolean) {
        viewModelScope.launch {
            if (isFollowing) {
                try {
                    analytics.unfollowClicked()
                    unfollowAccount(accountId, loggedInUserAccountId)
                } catch (e: UnfollowAccount.UnfollowFailedException) {
                    Timber.e(e)
                }
            } else {
                try {
                    analytics.followClicked()
                    followAccount(accountId, loggedInUserAccountId)
                } catch (e: FollowAccount.FollowFailedException) {
                    Timber.e(e)
                }
            }
        }
    }

    override fun onScreenViewed() {
        analytics.followersScreenViewed()
    }

    override fun onTabClicked(tabType: FollowType) {
        when (tabType) {
            FollowType.FOLLOWERS -> analytics.followersScreenViewed()
            FollowType.FOLLOWING -> analytics.followingScreenViewed()
        }
    }
}
