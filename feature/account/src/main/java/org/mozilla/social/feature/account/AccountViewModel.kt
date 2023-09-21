package org.mozilla.social.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mozilla.social.common.logging.Log
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.StatusRepository
import org.mozilla.social.core.domain.AccountIdFlow
import org.mozilla.social.core.domain.Logout
import org.mozilla.social.core.ui.postcard.PostCardDelegate
import org.mozilla.social.core.ui.postcard.PostCardNavigation
import org.mozilla.social.model.Account
import org.mozilla.social.model.Status

class AccountViewModel(
    private val accountRepository: AccountRepository,
    accountIdFlow: AccountIdFlow,
    log: Log,
    statusRepository: StatusRepository,
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
     */
    private val usersAccountId: StateFlow<String?> = accountIdFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    /**
     * if an account Id was passed in the constructor, the use that,
     * otherwise get the user's account Id
     */
    private val accountId: StateFlow<String?> = initialAccountId?.let {
        flowOf(it).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = it,
        )
    } ?: usersAccountId

    /**
     * true if we are viewing the logged in user's profile
     */
    private val isUsersProfile: StateFlow<Boolean> = usersAccountId.flatMapLatest {
        flowOf(initialAccountId == null || it != initialAccountId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false,
    )

    val account: Flow<Account> =
        accountId.flatMapLatest {
            if (!it.isNullOrBlank()) {
                getAccountForUser(it)
            } else flowOf()
        }


    val accountStatuses: Flow<List<Status>> =
        accountId.flatMapLatest {
            if (it != null) {
                getAccountStatuses(it)
            } else flowOf()
        }


    private fun getAccountForUser(accountId: String): Flow<Account> {
        return flow {
            val response = accountRepository.getAccount(accountId)
            try {
                emit(response)
            } catch (e: Exception) {

            }
        }
    }

    private fun getAccountStatuses(accountId: String): Flow<List<Status>> {
        return flow {
            val response = accountRepository.getAccountStatuses(accountId)
            try {
                emit(response)
            } catch (e: Exception) {

            }
        }
    }

    private fun getAccountBookmarks(): Flow<List<Status>> {
        return flow {
            val response = accountRepository.getAccountBookmarks()
            try {
                emit(response)
            } catch (e: Exception) {

            }
        }
    }

    private fun getAccountFavourites(): Flow<List<Status>> {
        return flow {
            val response = accountRepository.getAccountFavourites()
            try {
                emit(response)
            } catch (e: Exception) {

            }
        }
    }
}