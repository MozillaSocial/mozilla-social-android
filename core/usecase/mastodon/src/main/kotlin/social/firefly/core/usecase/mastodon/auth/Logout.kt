package social.firefly.core.usecase.mastodon.auth

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import social.firefly.common.appscope.AppScope
import social.firefly.core.analytics.AppAnalytics
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.repository.mastodon.DatabaseDelegate

/**
 * Handles data related cleanup.
 */
class Logout(
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
    private val databaseDelegate: DatabaseDelegate,
    private val analytics: AppAnalytics,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val appScope: AppScope,
) {
    operator fun invoke() =
        GlobalScope.launch(ioDispatcher) {
            appScope.reset()
            userPreferencesDatastoreManager.deleteDataStore(
                userPreferencesDatastoreManager.activeUserDatastore.first()
            )
            databaseDelegate.clearAllTables()

            /** Possible use of analytics...destroy() **/
            analytics.clearLoggedInIdentifiers()
        }
}
