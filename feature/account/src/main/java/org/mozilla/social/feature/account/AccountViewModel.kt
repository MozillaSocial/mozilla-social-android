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
import org.mozilla.social.core.network.model.NetworkAccount

class AccountViewModel(
    private val userPreferencesDatastore: UserPreferencesDatastore,
    private val accountRepository: AccountRepository,
    private val onLogout: () -> Unit
) : ViewModel() {

    private val accountId: StateFlow<String?> =
        userPreferencesDatastore.dataStore.data.map {
            it.accountId
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val account: Flow<NetworkAccount> =
        accountId.flatMapLatest {
            if (it != null) {
                getAccountForUser(it)
            } else flowOf()
        }

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

    private fun getAccountForUser(accountId: String): Flow<NetworkAccount> {
        return flow {
            val response = accountRepository.getAccount(accountId)
            try {
                emit(response)
            } catch(e: Exception) {

            }
        }
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
}