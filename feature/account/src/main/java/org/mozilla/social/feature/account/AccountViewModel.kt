package org.mozilla.social.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import org.mozilla.social.common.Resource
import org.mozilla.social.common.logging.Log
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.common.utils.edit
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.model.statusCollections.toStatusWrapper
import org.mozilla.social.core.domain.AccountIdBlocking
import org.mozilla.social.core.domain.GetDetailedAccount
import org.mozilla.social.core.navigation.NavigationDestination
import org.mozilla.social.core.navigation.usecases.NavigateTo
import org.mozilla.social.core.navigation.usecases.OpenLink
import org.mozilla.social.core.ui.common.postcard.PostCardDelegate
import org.mozilla.social.core.ui.common.postcard.toPostCardUiState
import org.mozilla.social.core.ui.common.R
import timber.log.Timber

class AccountViewModel(
    private val analytics: Analytics,
    private val accountRepository: AccountRepository,
    accountIdBlocking: AccountIdBlocking,
    log: Log,
    statusRepository: StatusRepository,
    private val socialDatabase: SocialDatabase,
    private val getDetailedAccount: GetDetailedAccount,
    private val navigateTo: NavigateTo,
    openLink: OpenLink,
    initialAccountId: String?,
) : ViewModel(), AccountInteractions {

    private val _errorToastMessage = MutableSharedFlow<StringFactory>(extraBufferCapacity = 1)
    val errorToastMessage = _errorToastMessage.asSharedFlow()

    val postCardDelegate = PostCardDelegate(
        coroutineScope = viewModelScope,
        statusRepository = statusRepository,
        accountRepository = accountRepository,
        log = log,
        navigateTo = navigateTo,
        openLink = openLink,
    )

    /**
     * The account ID of the logged in user
     */
    private val usersAccountId: String = accountIdBlocking()

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

    fun onAccountScreenShown() {
        analytics.uiImpression(
            mastodonAccountId = accountId,
            uiIdentifier = "account.screen.impression",
        )
    }

    override fun onOverflowMuteClicked() {
        viewModelScope.launch {
            try {
                accountRepository.muteAccount(accountId)
            } catch (e: Exception) {
                Timber.e(e)
                _errorToastMessage.emit(StringFactory.resource(R.string.error_muting_account))
            }
        }
    }

    override fun onOverflowUnmuteClicked() {
        viewModelScope.launch {
            try {
                accountRepository.unmuteAccount(accountId)
            } catch (e: Exception) {
                Timber.e(e)
                _errorToastMessage.emit(StringFactory.resource(R.string.error_unmuting_account))
            }
        }
    }

    override fun onOverflowBlockClicked() {
        viewModelScope.launch {
            try {
                accountRepository.blockAccount(accountId)
            } catch (e: Exception) {
                Timber.e(e)
                _errorToastMessage.emit(StringFactory.resource(R.string.error_blocking_account))
            }
        }
    }

    override fun onOverflowUnblockClicked() {
        viewModelScope.launch {
            try {
                accountRepository.unblockAccount(accountId)
            } catch (e: Exception) {
                Timber.e(e)
                _errorToastMessage.emit(StringFactory.resource(R.string.error_unblocking_account))
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
                accountRepository.followAccount(
                    accountId = accountId,
                    loggedInUserAccountId = usersAccountId
                )
            } catch (e: Exception) {
                Timber.e(e)
                _errorToastMessage.emit(StringFactory.resource(R.string.error_following_account))
            }
        }
    }

    override fun onUnfollowClicked() {
        viewModelScope.launch {
            try {
                accountRepository.unfollowAccount(
                    accountId = accountId,
                    loggedInUserAccountId = usersAccountId,
                )
            } catch (e: Exception) {
                Timber.e(e)
                _errorToastMessage.emit(StringFactory.resource(R.string.error_unfollowing_account))
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