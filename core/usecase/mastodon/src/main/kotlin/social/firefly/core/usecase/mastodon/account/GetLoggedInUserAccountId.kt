package social.firefly.core.usecase.mastodon.account

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.runBlocking
import social.firefly.core.datastore.UserPreferencesDatastoreManager

/**
 * Synchronously gets the account ID of the current logged in user
 */
class GetLoggedInUserAccountId(
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): String =
        runBlocking {
            userPreferencesDatastoreManager.activeUserDatastore.flatMapLatest {
                it.accountId
            }.first()
        }
}
