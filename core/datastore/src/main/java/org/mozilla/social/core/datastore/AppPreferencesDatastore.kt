package org.mozilla.social.core.datastore

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class AppPreferencesDatastore(context: Context) {
    private val dataStore = context.appPreferencesDataStore

    val trackAnalytics: Flow<Boolean> = dataStore.data.mapLatest {
        it.trackAnalytics
    }

    suspend fun toggleTrackAnalytics(trackAnalytics: Boolean) {
        dataStore.updateData {
            it.toBuilder()
                .setTrackAnalytics(trackAnalytics)
                .build()
        }
    }
}