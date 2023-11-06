package org.mozilla.social.feature.settings.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.mozilla.social.core.domain.AccountFlow
import org.mozilla.social.core.domain.Logout
import org.mozilla.social.model.Account
import timber.log.Timber

class AccountSettingsViewModel(
    private val logout: Logout,
    getAccountFlow: AccountFlow,
) : ViewModel() {

    val userHeader = try {
        getAccountFlow().mapLatest { it.toUserHeader() }
    } catch (exception: Exception) {
        Timber.e(exception)
        flowOf(null)
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            logout()
        }
    }
}

data class UserHeader(val avatarUrl: String, val accountName: String, val url: String)

fun Account.toUserHeader() = UserHeader(
    avatarUrl = avatarUrl,
    accountName = acct,
    url = url,
)