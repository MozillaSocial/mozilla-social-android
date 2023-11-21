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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import org.mozilla.social.common.Resource
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.model.statusCollections.toStatusWrapper
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.toPostCardUiState
import org.mozilla.social.core.usecase.mastodon.account.BlockAccount
import org.mozilla.social.core.usecase.mastodon.account.FollowAccount
import org.mozilla.social.core.usecase.mastodon.account.GetDetailedAccount
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.account.MuteAccount
import org.mozilla.social.core.usecase.mastodon.account.UnblockAccount
import org.mozilla.social.core.usecase.mastodon.account.UnfollowAccount
import org.mozilla.social.core.usecase.mastodon.account.UnmuteAccount
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
        PostCardDelegate::class.java,
    ) { parametersOf(viewModelScope, AnalyticsIdentifiers.FEED_PREFIX_PROFILE) }

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

    private val externalTimelineType: StateFlow<org.mozilla.social.core.usecase.mastodon.timeline.TimelineType> =
        timelineType.map { it.toExternalModel() }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            org.mozilla.social.core.usecase.mastodon.timeline.TimelineType.POSTS,
        )

    private val accountTimelineRemoteMediator: AccountTimelineRemoteMediator by inject(
        AccountTimelineRemoteMediator::class.java,
    ) {
        parametersOf(
            accountId,
            externalTimelineType,
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    val feed =
        Pager(
            config =
                PagingConfig(
                    pageSize = 20,
                    initialLoadSize = 40,
                ),
            remoteMediator = accountTimelineRemoteMediator,
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
        getAccountJob =
            viewModelScope.launch {
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

    override fun onOverflowShareClicked() {
        analytics.uiEngagement(
            uiIdentifier = AnalyticsIdentifiers.PROFILE_MORE_SHARE_ACCOUNT,
            mastodonAccountId = accountId,
            mastodonAccountHandle = usersAccountId
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
                ),
            )
        }
    }

    override fun onFollowersClicked() {
        uiState.value.data?.displayName?.let { displayName ->
            navigateTo(NavigationDestination.Followers(
                accountId = accountId,
                displayName = displayName,
                startingTab = NavigationDestination.Followers.StartingTab.FOLLOWERS,
            ))
        }
    }

    override fun onFollowingClicked() {
        uiState.value.data?.displayName?.let { displayName ->
            navigateTo(NavigationDestination.Followers(
                accountId = accountId,
                displayName = displayName,
                startingTab = NavigationDestination.Followers.StartingTab.FOLLOWING,
            ))
        }
    }

    override fun onFollowClicked() {
        viewModelScope.launch {
            try {
                analytics.uiEngagement(
                    uiIdentifier = AnalyticsIdentifiers.ACCOUNTS_SCREEN_FOLLOW,
                    mastodonAccountId = accountId,
                    mastodonAccountHandle = usersAccountId
                )
                followAccount(
                    accountId = accountId,
                    loggedInUserAccountId = usersAccountId,
                )
            } catch (e: FollowAccount.FollowFailedException) {
                Timber.e(e)
            }
        }
    }

    override fun onUnfollowClicked() {
        viewModelScope.launch {
            try {
                analytics.uiEngagement(
                    uiIdentifier = AnalyticsIdentifiers.ACCOUNTS_SCREEN_UNFOLLOW,
                    mastodonAccountId = accountId,
                    mastodonAccountHandle = usersAccountId
                )
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

private fun TimelineType.toExternalModel() =
    when (this) {
        TimelineType.POSTS -> {
            org.mozilla.social.core.usecase.mastodon.timeline.TimelineType.POSTS
        }
        TimelineType.POSTS_AND_REPLIES -> {
            org.mozilla.social.core.usecase.mastodon.timeline.TimelineType.POSTS_AND_REPLIES
        }
        TimelineType.MEDIA -> {
            org.mozilla.social.core.usecase.mastodon.timeline.TimelineType.MEDIA
        }
    }
