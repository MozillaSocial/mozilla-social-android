package social.firefly.feature.followers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import social.firefly.core.analytics.FollowersAnalytics
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.mastodon.FollowersRepository
import social.firefly.core.repository.mastodon.FollowingsRepository
import social.firefly.core.repository.paging.FollowersRemoteMediator
import social.firefly.core.repository.paging.FollowingsRemoteMediator
import social.firefly.core.ui.accountfollower.toAccountFollowerUiState
import social.firefly.core.ui.common.following.FollowStatus
import social.firefly.core.usecase.mastodon.account.FollowAccount
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId
import social.firefly.core.usecase.mastodon.account.UnfollowAccount
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
