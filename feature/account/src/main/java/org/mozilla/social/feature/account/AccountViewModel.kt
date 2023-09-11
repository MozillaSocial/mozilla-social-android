package org.mozilla.social.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.domain.AccountIdFlow
import org.mozilla.social.core.domain.IsSignedInFlow
import org.mozilla.social.core.domain.Logout
import org.mozilla.social.model.Account
import org.mozilla.social.model.Status

class AccountViewModel(
    private val accountRepository: AccountRepository,
    accountIdFlow: AccountIdFlow,
    private val logout: Logout,
) : ViewModel() {

    private val accountId: StateFlow<String?> =
        accountIdFlow().stateIn(
            scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = null,
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

//    val accountBookmarks: Flow<List<NetworkStatus>> =
//        accountId.flatMapLatest {
//            if (it != null) {
//                getAccountBookmarks()
//            } else flowOf()
//        }
//
//    val accountFavourites: Flow<List<NetworkStatus>> =
//        accountId.flatMapLatest {
//            if (it != null) {
//                getAccountFavourites()
//            } else flowOf()
//        }


    private fun getAccountForUser(accountId: String): Flow<Account> {
        return flow {
            val response = accountRepository.getAccount(accountId)
            try {
                emit(response)
            } catch (e: Exception) {

            }
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            logout()
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