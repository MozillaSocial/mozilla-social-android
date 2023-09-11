package org.mozilla.social.core.domain

import org.mozilla.social.core.datastore.UserPreferencesDatastore

class Logout(
    private val userPreferencesDatastore: UserPreferencesDatastore,
) {
    suspend operator fun invoke() {
        userPreferencesDatastore.clearData()
        // TODO clear out DB and any other data
    }
}