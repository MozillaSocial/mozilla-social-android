package social.firefly.core.usecase.mastodon.auth

import social.firefly.core.datastore.UserPreferencesDatastore

class IsSignedInFlow(private val userPreferencesDatastore: UserPreferencesDatastore) {
    operator fun invoke() = userPreferencesDatastore.isSignedIn
}
