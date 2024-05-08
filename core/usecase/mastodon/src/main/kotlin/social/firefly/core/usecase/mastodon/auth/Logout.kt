package social.firefly.core.usecase.mastodon.auth

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import social.firefly.common.appscope.AppScope
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.mastodon.DatabaseDelegate

/**
 * Handles data related cleanup.
 */
class Logout(
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
    private val databaseDelegate: DatabaseDelegate,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val appScope: AppScope,
    private val navigateTo: NavigateTo,
) {
    @OptIn(DelicateCoroutinesApi::class)
    operator fun invoke(accountId: String, domain: String) =
        GlobalScope.launch(ioDispatcher) {
            appScope.reset()
            val accountToDelete = userPreferencesDatastoreManager.dataStores.value.find {
                it.accountId.first() == accountId && it.domain.first() == domain
            }
            if (accountToDelete == null) return@launch

            val isDeletingActiveUserDataStore = userPreferencesDatastoreManager.deleteDataStore(accountToDelete)

            // logging out of active account
            if (isDeletingActiveUserDataStore) {
                if (!userPreferencesDatastoreManager.isLoggedInToAtLeastOneAccount) {
                    navigateTo(NavigationDestination.Auth)
                } else {
                    navigateTo(NavigationDestination.Tabs)
                }
                databaseDelegate.clearAllTables()
            }
        }
}
