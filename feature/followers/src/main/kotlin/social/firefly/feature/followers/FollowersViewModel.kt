package social.firefly.feature.followers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import social.firefly.core.analytics.FollowersAnalytics
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.paging.pagers.accounts.FollowersPager
import social.firefly.core.repository.paging.pagers.accounts.FollowingsPager
import social.firefly.core.ui.accountfollower.toAccountFollowerUiState
import social.firefly.core.ui.common.following.FollowStatus
import social.firefly.core.usecase.mastodon.account.FollowAccount
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId
import social.firefly.core.usecase.mastodon.account.UnfollowAccount
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class FollowersViewModel(
    private val accountId: String,
    private val navigateTo: NavigateTo,
    private val analytics: FollowersAnalytics,
    private val followAccount: FollowAccount,
    private val unfollowAccount: UnfollowAccount,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
) : ViewModel(), FollowersInteractions, KoinComponent {

    private val loggedInUserAccountId = getLoggedInUserAccountId()

    private val followersPager: FollowersPager by inject {
        parametersOf(accountId)
    }

    private val followingsPager: FollowingsPager by inject {
        parametersOf(accountId)
    }

    val followers = followersPager.build().map { pagingData ->
        pagingData.map {
            it.toAccountFollowerUiState(loggedInUserAccountId)
        }
    }.cachedIn(viewModelScope)

    val following = followingsPager.build().map { pagingData ->
        pagingData.map {
            it.toAccountFollowerUiState(loggedInUserAccountId)
        }
    }.cachedIn(viewModelScope)

    override fun onAccountClicked(accountId: String) {
        navigateTo(NavigationDestination.Account(accountId))
    }

    override fun onFollowClicked(accountId: String, followStatus: FollowStatus) {
        viewModelScope.launch {
            when (followStatus) {
                FollowStatus.FOLLOWING,
                FollowStatus.PENDING_REQUEST -> {
                    try {
                        analytics.unfollowClicked()
                        unfollowAccount(accountId, loggedInUserAccountId)
                    } catch (e: UnfollowAccount.UnfollowFailedException) {
                        Timber.e(e)
                    }
                }
                FollowStatus.NOT_FOLLOWING -> {
                    try {
                        analytics.followClicked()
                        followAccount(accountId, loggedInUserAccountId)
                    } catch (e: FollowAccount.FollowFailedException) {
                        Timber.e(e)
                    }
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
