package org.mozilla.social.core.domain

import kotlinx.coroutines.flow.Flow
import org.mozilla.social.core.datastore.UserPreferencesDatastore

class AccountIdFlow(private val userPreferencesDatastore: UserPreferencesDatastore) {

    operator fun invoke(): Flow<String> = userPreferencesDatastore.accountId
}