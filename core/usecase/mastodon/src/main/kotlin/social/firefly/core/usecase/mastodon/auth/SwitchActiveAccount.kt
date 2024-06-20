package social.firefly.core.usecase.mastodon.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import social.firefly.core.accounts.AccountsManager
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.mastodon.DatabaseDelegate

class SwitchActiveAccount(
    private val databaseDelegate: DatabaseDelegate,
    private val navigateTo: NavigateTo,
    private val accountsManager: AccountsManager,
){

    suspend operator fun invoke(
        accountId: String,
        domain: String,
    ) {
        accountsManager.getAllAccounts().find {
            it.accountId == accountId && it.domain == domain
        }?.let {
            accountsManager.setActiveAccount(it)
        }

        withContext(Dispatchers.IO) {
            databaseDelegate.clearAllTables()
        }
        navigateTo(NavigationDestination.Tabs)
    }
}