package social.firefly.core.usecase.mastodon.account

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import social.firefly.core.datastore.UserPreferencesDatastoreManager

class GetDomain(
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<String> = userPreferencesDatastoreManager.activeUserDatastore.flatMapLatest {
        it.domain
    }
}