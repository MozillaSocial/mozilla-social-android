package org.mozilla.social.core.domain

import org.mozilla.social.core.datastore.UserPreferencesDatastore

class IsSignedInFlow(private val userPreferencesDatastore: UserPreferencesDatastore) {

    operator fun invoke() =
        userPreferencesDatastore.isSignedIn

}