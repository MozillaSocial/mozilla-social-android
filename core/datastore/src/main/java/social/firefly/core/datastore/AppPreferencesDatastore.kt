package social.firefly.core.datastore

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class AppPreferencesDatastore(context: Context) {
    private val dataStore = context.appPreferencesDataStore

    val allowAnalytics: Flow<Boolean> =
        dataStore.data.mapLatest {
            it.trackAnalytics
        }

    suspend fun allowAnalytics(allowAnalytics: Boolean) {
        dataStore.updateData {
            it.toBuilder()
                .setTrackAnalytics(allowAnalytics)
                .build()
        }
    }
}
