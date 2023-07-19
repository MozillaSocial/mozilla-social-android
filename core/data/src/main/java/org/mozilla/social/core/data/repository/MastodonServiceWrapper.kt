package org.mozilla.social.core.data.repository

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import org.mozilla.social.core.network.MastodonService

/**
 * This class serves a MastodonService in a flow, once it's initialized with a valid access token
 */
class MastodonServiceWrapper(userPreferencesDatastore: UserPreferencesDatastore) {

    val service: StateFlow<MastodonService?> = userPreferencesDatastore.dataStore.data.mapLatest {
        if (it.accessToken != null) {
            MastodonService(it.accessToken)
        } else null
    }.stateIn(GlobalScope, SharingStarted.WhileSubscribed(), initialValue = null)
}