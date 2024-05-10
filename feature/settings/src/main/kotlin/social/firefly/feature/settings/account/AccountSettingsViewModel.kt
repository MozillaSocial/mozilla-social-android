package social.firefly.feature.settings.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import social.firefly.core.analytics.SettingsAnalytics
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.navigation.AuthNavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.navigation.usecases.OpenLink
import social.firefly.core.usecase.mastodon.auth.Logout
import social.firefly.core.usecase.mastodon.auth.LogoutOfAllAccounts
import social.firefly.core.usecase.mastodon.auth.SwitchActiveAccount
import social.firefly.core.usecase.mastodon.auth.UpdateAllLoggedInAccounts
import timber.log.Timber

class AccountSettingsViewModel(
    private val logout: Logout,
    private val openLink: OpenLink,
    private val analytics: SettingsAnalytics,
    private val navigateTo: NavigateTo,
    private val switchActiveAccount: SwitchActiveAccount,
    private val logoutOfAllAccounts: LogoutOfAllAccounts,
    userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
    updateAllLoggedInAccounts: UpdateAllLoggedInAccounts,
) : ViewModel(), AccountSettingsInteractions {

    val otherAccounts = userPreferencesDatastoreManager.dataStores.combine(
        userPreferencesDatastoreManager.activeUserDatastore
    ) { dataStores, activeDataStore ->
        dataStores.filterNot {
            it == activeDataStore
        }.map { dataStore ->
            LoggedInAccount(
                accountId = dataStore.accountId,
                userName = dataStore.userName,
                domain = dataStore.domain,
                avatarUrl = dataStore.avatarUrl,
            )
        }
    }

    val activeAccount = userPreferencesDatastoreManager.activeUserDatastore.map { dataStore ->
        LoggedInAccount(
            accountId = dataStore.accountId,
            userName = dataStore.userName,
            domain = dataStore.domain,
            avatarUrl = dataStore.avatarUrl,
        )
    }

    init {
        viewModelScope.launch {
            try {
                updateAllLoggedInAccounts()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    override fun onLogoutClicked(accountId: String, domain: String) {
        analytics.logoutClicked()
        viewModelScope.launch {
            logout(
                accountId = accountId,
                domain = domain,
            )
        }
    }

    override fun onManageAccountClicked(domain: String) {
        openLink("$domain/settings/profile")
    }

    override fun onAddAccountClicked() {
        navigateTo(AuthNavigationDestination.ChooseServer)
    }

    override fun onSetAccountAsActiveClicked(accountId: String, domain: String) {
        viewModelScope.launch {
            switchActiveAccount(accountId, domain)
        }
    }

    override fun onLogoutOfAllAccountsClicked() {
        viewModelScope.launch {
            logoutOfAllAccounts()
        }
    }

    override fun onScreenViewed() {
        analytics.accountSettingsViewed()
    }
}

