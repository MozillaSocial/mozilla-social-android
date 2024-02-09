package org.mozilla.social.feature.settings.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mozilla.social.common.loadResource
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.navigation.usecases.OpenLink
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.usecase.mastodon.account.GetDomain
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.auth.Logout
import org.mozilla.social.feature.settings.R
import org.mozilla.social.core.analytics.SettingsAnalytics

class AccountSettingsViewModel(
    private val logout: Logout,
    private val openLink: OpenLink,
    private val analytics: SettingsAnalytics,
    private val getDomain: GetDomain,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    accountRepository: AccountRepository,
) : ViewModel(), AccountSettingsInteractions {

    private val userAccountId: String = getLoggedInUserAccountId()
    private val domain = getDomain().stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val subtitle: StateFlow<StringFactory?> = domain.map { domain ->
        domain?.let { StringFactory.resource(R.string.manage_your_account, it) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val userHeader =
        loadResource {
            accountRepository.getAccount(userAccountId).toUserHeader()
        }


    override fun onLogoutClicked() {
        analytics.logoutClicked()
        viewModelScope.launch {
            logout()
        }
    }

    override fun onManageAccountClicked() {
        domain.value?.let { openLink("$it/settings/profile") }
    }


    override fun onScreenViewed() {
        analytics.accountSettingsViewed()
    }
}

data class UserHeader(val avatarUrl: String, val accountName: String, val url: String)

fun Account.toUserHeader() =
    UserHeader(
        avatarUrl = avatarUrl,
        accountName = acct,
        url = url,
    )
