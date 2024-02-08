package org.mozilla.social.core.usecase.mastodon.auth

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.mozilla.social.common.appscope.AppScope
import org.mozilla.social.core.analytics.AppAnalytics
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import org.mozilla.social.core.repository.mastodon.DatabaseDelegate

/**
 * Handles data related cleanup.
 */
class Logout(
    private val userPreferencesDatastore: UserPreferencesDatastore,
    private val databaseDelegate: DatabaseDelegate,
    private val analytics: AppAnalytics,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val appScope: AppScope,
) {
    operator fun invoke() =
        GlobalScope.launch(ioDispatcher) {
            appScope.reset()
            userPreferencesDatastore.clearData()
            databaseDelegate.clearAllTables()

            /** Possible use of analytics...destroy() **/
            analytics.clearLoggedInIdentifiers()
        }
}
