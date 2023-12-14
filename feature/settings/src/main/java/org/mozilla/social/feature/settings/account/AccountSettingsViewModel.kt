package org.mozilla.social.feature.settings.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mozilla.social.common.loadResource
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.analytics.AnalyticsIdentifiers
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.navigation.usecases.OpenLink
import org.mozilla.social.core.repository.mastodon.AccountRepository
import org.mozilla.social.core.usecase.mastodon.account.GetDomain
import org.mozilla.social.core.usecase.mastodon.account.GetLoggedInUserAccountId
import org.mozilla.social.core.usecase.mastodon.auth.Logout
import org.mozilla.social.feature.settings.SettingsInteractions

class AccountSettingsViewModel(
    private val logout: Logout,
    private val openLink: OpenLink,
    private val analytics: Analytics,
    private val getDomain: GetDomain,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    accountRepository: AccountRepository,
) : ViewModel(), SettingsInteractions {

    private val userAccountId: String = getLoggedInUserAccountId()

    val userHeader =
        loadResource {
            accountRepository.getAccount(userAccountId).toUserHeader()
        }

    val domain = getDomain()

    fun onLogoutClicked() {
        logoutClickedAnalytics()
        viewModelScope.launch {
            logout()
        }
    }

    private fun logoutClickedAnalytics() {
        analytics.uiEngagement(
            uiIdentifier = AnalyticsIdentifiers.SETTINGS_ACCOUNT_SIGNOUT,
            mastodonAccountId = userAccountId
        )
    }

    fun onManageAccountClicked() {
        logoutClickedAnalytics()
        viewModelScope.launch {
            getDomain().collect {
                openLink("$it/settings/profile")
            }
        }
    }


    override fun onScreenViewed() {
        analytics.uiImpression(
            uiIdentifier = AnalyticsIdentifiers.SETTINGS_ACCOUNT_IMPRESSION,
            mastodonAccountId = userAccountId,
        )
    }
}

data class UserHeader(val avatarUrl: String, val accountName: String, val url: String)

fun Account.toUserHeader() =
    UserHeader(
        avatarUrl = avatarUrl,
        accountName = acct,
        url = url,
    )
