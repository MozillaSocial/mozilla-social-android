package social.firefly.core.usecase.mastodon.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import social.firefly.core.datastore.AppPreferencesDatastore
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.navigation.NavigationDestination
import social.firefly.core.navigation.usecases.NavigateTo
import social.firefly.core.repository.mastodon.DatabaseDelegate

class SwitchActiveAccount(
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
    private val appPreferencesDatastore: AppPreferencesDatastore,
    private val databaseDelegate: DatabaseDelegate,
    private val navigateTo: NavigateTo,
){

    suspend operator fun invoke(
        accountId: String,
        domain: String,
    ) {
        userPreferencesDatastoreManager.dataStores.value.find {
            it.accountId.first() == accountId && it.domain.first() == domain
        }?.let {
            appPreferencesDatastore.saveActiveUserDatastoreFilename(it.fileName)
        }

        withContext(Dispatchers.IO) {
            databaseDelegate.clearAllTables()
        }
        navigateTo(NavigationDestination.Tabs)
    }
}