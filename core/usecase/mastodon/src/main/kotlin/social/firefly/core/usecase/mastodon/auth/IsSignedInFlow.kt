package social.firefly.core.usecase.mastodon.auth

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import social.firefly.core.datastore.UserPreferencesDatastoreManager

class IsSignedInFlow(
    private val userPreferencesDatastoreManager: UserPreferencesDatastoreManager
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke() = userPreferencesDatastoreManager.activeUserDatastore.flatMapLatest {
        it.isSignedIn
    }
}
