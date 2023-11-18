package org.mozilla.social.core.usecase.mastodon.account

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import timber.log.Timber

/**
 * Synchronously gets the account ID of the current logged in user
 */
class GetLoggedInUserAccountId(
    private val userPreferencesDatastore: UserPreferencesDatastore,
) {
    operator fun invoke(): String =
        runBlocking {
            userPreferencesDatastore.accountId.first()
                .also { if (it.isBlank()) Timber.e("account id was blank") }
        }
}
