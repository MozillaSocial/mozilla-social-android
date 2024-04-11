package social.firefly.core.usecase.mastodon.account

import kotlinx.coroutines.flow.Flow
import social.firefly.core.datastore.UserPreferencesDatastore

class GetDomain(private val userPreferencesDatastore: UserPreferencesDatastore) {
    operator fun invoke(): Flow<String> = userPreferencesDatastore.domain
}