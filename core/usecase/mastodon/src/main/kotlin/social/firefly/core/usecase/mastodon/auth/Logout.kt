package social.firefly.core.usecase.mastodon.auth

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import social.firefly.common.appscope.AppScope
import social.firefly.core.accounts.AccountsManager
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.mastodon.DatabaseDelegate

/**
 * Handles data related cleanup.
 */
class Logout(
    private val databaseDelegate: DatabaseDelegate,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val appScope: AppScope,
    private val navigateTo: NavigateTo,
    private val accountsManager: AccountsManager,
) {
    @OptIn(DelicateCoroutinesApi::class)
    operator fun invoke(accountId: String, domain: String) =
        GlobalScope.launch(ioDispatcher) {
            appScope.reset()
            val accounts = accountsManager.getAllAccounts()
            val activeAccount = accountsManager.getActiveAccount()
            val accountToDelete = accounts.find {
                it.accountId == accountId && it.domain == domain
            }
            if (accountToDelete == null) return@launch

            val isDeletingActiveUserDataStore = accountToDelete == activeAccount
            accountsManager.deleteAccount(
                accountId = accountId,
                domain = domain,
            )

            // logging out of active account
            if (isDeletingActiveUserDataStore) {
                if (accounts.size <= 1) {
                    navigateTo(NavigationDestination.Auth)
                } else {
                    navigateTo(NavigationDestination.Tabs)
                }
                databaseDelegate.clearAllTables()
            }
        }
}
