package org.mozilla.social.core.data.repository

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import org.mozilla.social.core.network.MastodonService
import retrofit2.Retrofit

/**
 * This class serves a MastodonService in a flow, once it's initialized with a valid access token
 */
class MastodonServiceWrapper(userPreferencesDatastore: UserPreferencesDatastore) {

    private val authToken: StateFlow<String?> = userPreferencesDatastore.dataStore.data.mapLatest {
        it.accessToken
    }.stateIn(GlobalScope, SharingStarted.WhileSubscribed(), initialValue = null)

    val service: StateFlow<MastodonService?> = authToken.mapLatest {accessToken ->
        if (accessToken != null) {
            MastodonService(accessToken)
        } else null
    }.stateIn(GlobalScope, SharingStarted.WhileSubscribed(), initialValue = null)
}