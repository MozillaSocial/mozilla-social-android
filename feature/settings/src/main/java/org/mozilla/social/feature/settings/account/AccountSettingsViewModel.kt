package org.mozilla.social.feature.settings.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mozilla.social.common.loadResource
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.auth.Logout
import org.mozilla.social.model.Account

class AccountSettingsViewModel(
    private val logout: Logout,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    accountRepository: AccountRepository,
) : ViewModel() {

    val userHeader = loadResource {
        accountRepository.getAccount(getLoggedInUserAccountId()).toUserHeader()
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