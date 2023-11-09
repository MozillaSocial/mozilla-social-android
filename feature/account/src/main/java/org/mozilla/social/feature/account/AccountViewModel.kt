package org.mozilla.social.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject
import org.mozilla.social.common.Resource
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.model.statusCollections.toStatusWrapper
import org.mozilla.social.core.domain.account.FollowAccount
import org.mozilla.social.core.domain.GetDetailedAccount
import org.mozilla.social.core.domain.GetLoggedInUserAccountId
import org.mozilla.social.core.domain.account.BlockAccount
import org.mozilla.social.core.domain.account.MuteAccount
import org.mozilla.social.core.domain.account.UnblockAccount
import org.mozilla.social.core.domain.account.UnfollowAccount
import org.mozilla.social.core.domain.account.UnmuteAccount
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.toPostCardUiState
import timber.log.Timber

class AccountViewModel(
    private val analytics: Analytics,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    private val socialDatabase: SocialDatabase,
    private val getDetailedAccount: GetDetailedAccount,
    private val navigateTo: NavigateTo,
    private val followAccount: FollowAccount,
    private val unfollowAccount: UnfollowAccount,
    private val blockAccount: BlockAccount,
    private val unblockAccount: UnblockAccount,
    private val muteAccount: MuteAccount,
    private val unmuteAccount: UnmuteAccount,
    initialAccountId: String?,
) : ViewModel(), AccountInteractions {

    val postCardDelegate: PostCardDelegate by inject(
        PostCardDelegate::class.java
    ) { parametersOf(viewModelScope) }

    /**
     * The account ID of the logged in user
     */
    private val usersAccountId: String = getLoggedInUserAccountId()

    /**
     * if an account Id was passed in the constructor, the use that,
     * otherwise get the user's account Id
     */
    private val accountId: String = initialAccountId ?: usersAccountId

    /**
     * true if we are viewing the logged in user's profile
     */
    val isOwnProfile = usersAccountId == accountId

    val shouldShowCloseButton = initialAccountId != null

    private val _uiState = MutableStateFlow<Resource<AccountUiState>>(Resource.Loading())
    val uiState = _uiState.asStateFlow()

    private var getAccountJob: Job? = null

    private val _timelineType = MutableStateFlow(TimelineType.POSTS)
    val timelineType = _timelineType.asStateFlow()

    private val accountTimelineRemoteMediator: AccountTimelineRemoteMediator by KoinJavaComponent.inject(
        AccountTimelineRemoteMediator::class.java
    ) {
        parametersOf(
            accountId,
            timelineType,
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    val feed = Pager(
        config = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40
        ),
        remoteMediator = accountTimelineRemoteMediator
    ) {
        socialDatabase.accountTimelineDao().accountTimelinePagingSource(accountId)
    }.flow.map { pagingData ->
        pagingData.map {
            it.toStatusWrapper().toExternalModel().toPostCardUiState(usersAccountId)
        }
    }.cachedIn(viewModelScope)

    init {
        loadAccount()
    }

    private fun loadAccount() {
        // ensure only one job happens at a time
        getAccountJob?.cancel()
        getAccountJob = viewModelScope.launch {
            getDetailedAccount(
                accountId = accountId,
                coroutineScope = viewModelScope,
            ) { account, relationship ->
                account.toUiState(relationship)
            }.collect {
                _uiState.edit { it }
            }
        }
    }

    override fun onScreenViewed() {
        analytics.uiImpression(
            mastodonAccountId = accountId,
            uiIdentifier = AnalyticsIdentifiers.ACCOUNTS_SCREEN_IMPRESSION,
        )
    }

    override fun onOverflowMuteClicked() {
        viewModelScope.launch {
            try {
                muteAccount(accountId)
            } catch (e: MuteAccount.MuteFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onOverflowUnmuteClicked() {
        viewModelScope.launch {
            try {
                unmuteAccount(accountId)
            } catch (e: UnmuteAccount.UnmuteFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onOverflowBlockClicked() {
        viewModelScope.launch {
            try {
                blockAccount(accountId)
            } catch (e: BlockAccount.BlockFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onOverflowUnblockClicked() {
        viewModelScope.launch {
            try {
                unblockAccount(accountId)
            } catch (e: UnblockAccount.UnblockFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onOverflowReportClicked() {
        (uiState.value as? Resource.Loaded)?.data?.webFinger?.let { webFinger ->
            navigateTo(
                NavigationDestination.Report(
                    accountId,
                    webFinger,
                )
            )
        }
    }

    override fun onFollowersClicked() {
        navigateTo(NavigationDestination.Followers(accountId))
    }

    override fun onFollowingClicked() {
        navigateTo(NavigationDestination.Following(accountId))
    }

    override fun onFollowClicked() {
        viewModelScope.launch {
            try {
                followAccount(
                    accountId = accountId,
                    loggedInUserAccountId = usersAccountId
                )
            } catch (e: FollowAccount.FollowFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onUnfollowClicked() {
        viewModelScope.launch {
            try {
                unfollowAccount(
                    accountId = accountId,
                    loggedInUserAccountId = usersAccountId,
                )
            } catch (e: UnfollowAccount.UnfollowFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onRetryClicked() {
        loadAccount()
    }

    override fun onTabClicked(timelineType: TimelineType) {
        _timelineType.edit { timelineType }
    }

    override fun onSettingsClicked() {
        navigateTo(NavigationDestination.Settings)
    }

    override fun onEditAccountClicked() {
        navigateTo(NavigationDestination.EditAccount)
    }
}