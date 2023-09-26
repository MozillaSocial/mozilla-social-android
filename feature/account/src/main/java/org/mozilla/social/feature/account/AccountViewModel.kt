package org.mozilla.social.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent
import org.mozilla.social.common.Resource
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.model.statusCollections.toStatusWrapper
import org.mozilla.social.core.domain.AccountIdFlow
import org.mozilla.social.core.domain.GetDetailedAccount
import org.mozilla.social.core.domain.remotemediators.AccountTimelineRemoteMediator
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.PostCardNavigation
import org.mozilla.social.core.ui.postcard.toPostCardUiState

class AccountViewModel(
    accountRepository: AccountRepository,
    accountIdFlow: AccountIdFlow,
    log: Log,
    statusRepository: StatusRepository,
    socialDatabase: SocialDatabase,
    getDetailedAccount: GetDetailedAccount,
    initialAccountId: String?,
    postCardNavigation: PostCardNavigation,
) : ViewModel() {

    val postCardDelegate = PostCardDelegate(
        coroutineScope = viewModelScope,
        statusRepository = statusRepository,
        accountRepository = accountRepository,
        log = log,
        postCardNavigation = postCardNavigation,
    )

    /**
     * The account ID of the logged in user
     *
     * So much depends on this value, trying out a blocking call to simplify the rest of the code.
     *
     * Uses a mutex so we can cancel the flow collection after we get the value.
     */
    private val usersAccountId: String = runBlocking {
        var value = ""

        val mutex = Mutex(true)
        val job = launch {
            accountIdFlow().collect {
                value = it
                mutex.unlock()
            }
        }
        mutex.lock()
        job.cancel()

        value
    }

    /**
     * if an account Id was passed in the constructor, the use that,
     * otherwise get the user's account Id
     */
    private val accountId: String = initialAccountId ?: usersAccountId

    /**
     * true if we are viewing the logged in user's profile
     */
    val isUsersProfile = usersAccountId == accountId

    val shouldShowTopBar = initialAccountId != null

    val uiState: Flow<Resource<AccountUiState>> = getDetailedAccount(
        accountId = accountId,
        coroutineScope = viewModelScope,
    ) { account, relationship ->
        account.toUiState(relationship)
    }

    private val accountTimelineRemoteMediator: AccountTimelineRemoteMediator by KoinJavaComponent.inject(
        AccountTimelineRemoteMediator::class.java
    ) { parametersOf(accountId) }

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
}