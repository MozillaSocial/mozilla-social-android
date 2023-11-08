package org.mozilla.social.core.domain

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import timber.log.Timber

/**
 * The account ID of the logged in user
 *
 * So much depends on this value, trying out a blocking call to simplify the rest of the code.
 *
 * Uses a mutex so we can cancel the flow collection after we get the value.
 */
class GetLoggedInUserAccountId(
    private val userPreferencesDatastore: UserPreferencesDatastore,
) {

    operator fun invoke(): String = runBlocking {
        userPreferencesDatastore.accountId.first()
            .also { if (it.isBlank()) Timber.e("account id was blank") }
    }
}