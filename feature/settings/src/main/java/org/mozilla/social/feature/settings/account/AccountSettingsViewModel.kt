package org.mozilla.social.feature.settings.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mozilla.social.common.loadResource
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.domain.AccountIdBlocking
import org.mozilla.social.core.domain.Logout
import org.mozilla.social.model.Account

class AccountSettingsViewModel(
    private val logout: Logout,
    accountIdBlocking: AccountIdBlocking,
    accountRepository: AccountRepository,
) : ViewModel() {

    val userHeader = loadResource {
        val account = accountRepository.getAccount(accountIdBlocking())
        UserHeader(avatarUrl = account.avatarUrl, accountName = account.acct, url = account.url)
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