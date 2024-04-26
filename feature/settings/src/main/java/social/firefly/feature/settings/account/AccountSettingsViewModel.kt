package social.firefly.feature.settings.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import social.firefly.common.loadResource
import social.firefly.common.utils.StringFactory
import social.firefly.core.analytics.SettingsAnalytics
import social.firefly.core.model.Account
import social.firefly.core.navigation.usecases.OpenLink
import social.firefly.core.repository.mastodon.AccountRepository
import social.firefly.core.usecase.mastodon.account.GetDomain
import social.firefly.core.usecase.mastodon.account.GetLoggedInUserAccountId
import social.firefly.core.usecase.mastodon.auth.Logout
import social.firefly.feature.settings.R

class AccountSettingsViewModel(
    private val logout: Logout,
    private val openLink: OpenLink,
    private val analytics: SettingsAnalytics,
    getDomain: GetDomain,
    getLoggedInUserAccountId: GetLoggedInUserAccountId,
    accountRepository: AccountRepository,
) : ViewModel(), AccountSettingsInteractions {

    private val userAccountId: String = getLoggedInUserAccountId()
    private val domain = getDomain()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    val subtitle: StateFlow<StringFactory?> = domain.map { domain ->
        domain?.let { StringFactory.resource(R.string.manage_your_account, it) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null,
    )

    val userHeader = loadResource {
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

data class UserHeader(
    val avatarUrl: String,
    val accountName: String,
    val url: String,
)

fun Account.toUserHeader() = UserHeader(
    avatarUrl = avatarUrl,
    accountName = acct,
    url = url,
)
