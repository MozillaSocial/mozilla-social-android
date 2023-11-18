package org.mozilla.social.core.usecase.mastodon.auth

import org.mozilla.social.core.datastore.UserPreferencesDatastore

class IsSignedInFlow(private val userPreferencesDatastore: UserPreferencesDatastore) {
    operator fun invoke() = userPreferencesDatastore.isSignedIn
}
