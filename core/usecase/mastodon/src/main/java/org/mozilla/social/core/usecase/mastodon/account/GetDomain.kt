package org.mozilla.social.core.usecase.mastodon.account

import kotlinx.coroutines.flow.Flow
import org.mozilla.social.core.datastore.UserPreferencesDatastore

class GetDomain(private val userPreferencesDatastore: UserPreferencesDatastore) {
    operator fun invoke(): Flow<String> = userPreferencesDatastore.domain
}