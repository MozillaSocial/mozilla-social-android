package org.mozilla.social.feature.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import org.mozilla.social.model.Account
import org.mozilla.social.model.Status

class AccountViewModel(
    private val userPreferencesDatastore: UserPreferencesDatastore,
    private val accountRepository: AccountRepository,
    private val userFollowers: () -> Unit,
    private val userFollowing: () -> Unit,
    private val onLogout: () -> Unit
) : ViewModel() {

    private val accountId: StateFlow<String?> =
        userPreferencesDatastore.dataStore.data.map {
            it.accountId
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val account: Flow<Account> =
        accountId.flatMapLatest {
            if (it != null) {
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

    init {
        viewModelScope.launch {
            userPreferencesDatastore.dataStore.data.map {
                !it.accessToken.isNullOrBlank()
            }.map { signedIn ->
                if (!signedIn) {
                    onLogout()
                }
            }.collect()
        }
        viewModelScope.launch {
            if (accountId.value.isNullOrBlank()) {
                userPreferencesDatastore.dataStore.updateData {
                    it.toBuilder()
                        .setAccountId(accountRepository.getUserAccount().accountId)
                        .build()
                }
            }
        }
    }

    private fun getAccountForUser(accountId: String): Flow<Account> {
        return flow {
            val response = accountRepository.getAccount(accountId)
            try {
                emit(response)
            } catch(e: Exception) {

            }
        }
    }

    fun showUsersFollowing() {
        userFollowing()
    }

    fun showUsersFollowers() {
        userFollowers()
    }

    fun logoutUser() {
        viewModelScope.launch {
            userPreferencesDatastore.dataStore.updateData {
                it.toBuilder()
                    .setAccessToken(null)
                    .setAccountId(null)
                    .build()
            }
        }
    }

    private fun getAccountStatuses(accountId: String): Flow<List<Status>> {
        return flow {
            val response = accountRepository.getAccountStatuses(accountId)
            try {
                emit(response)
            } catch(e: Exception) {

            }
        }
    }

    private fun getAccountBookmarks(): Flow<List<Status>> {
        return flow {
            val response = accountRepository.getAccountBookmarks()
            try {
                emit(response)
            } catch(e: Exception) {

            }
        }
    }

    private fun getAccountFavourites(): Flow<List<Status>> {
        return flow {
            val response = accountRepository.getAccountFavourites()
            try {
                emit(response)
            } catch(e: Exception) {

            }
        }
    }
}