package org.mozilla.social.core.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.mozilla.social.core.analytics.Analytics
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.datastore.UserPreferencesDatastore

/**
 * Handles data related cleanup.
 */
class Logout(
    private val userPreferencesDatastore: UserPreferencesDatastore,
    private val socialDatabase: SocialDatabase,
    private val analytics: Analytics,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    operator fun invoke() = GlobalScope.launch(ioDispatcher) {
        userPreferencesDatastore.clearData()
        socialDatabase.clearAllTables()

        /** Possible use of analytics...destroy() **/
        analytics.clearLoggedInIdentifiers()
    }
}