package social.firefly.core.datastore

import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import social.firefly.core.datastore.AppPreferences.ThemeType

@OptIn(ExperimentalCoroutinesApi::class)
class AppPreferencesDatastore(context: Context) {
    private val dataStore = context.appPreferencesDataStore

    val allowAnalytics: Flow<Boolean> =
        dataStore.data.mapLatest {
            it.trackAnalytics
        }.distinctUntilChanged()

    suspend fun allowAnalytics(allowAnalytics: Boolean) {
        dataStore.updateData {
            it.toBuilder()
                .setTrackAnalytics(allowAnalytics)
                .build()
        }
    }

    val themeType: Flow<ThemeType> =
        dataStore.data.mapLatest {
            it.themeType
        }.distinctUntilChanged()

    suspend fun saveThemeType(themeType: ThemeType) {
        dataStore.updateData {
            it.toBuilder()
                .setThemeType(themeType)
                .build()
        }
    }

    val activeUserDatastoreFilename: Flow<String> =
        dataStore.data.mapLatest {
            it.activeUserDatastoreFilename
        }.distinctUntilChanged()

    suspend fun saveActiveUserDatastoreFilename(filename: String) {
        dataStore.updateData {
            it.toBuilder()
                .setActiveUserDatastoreFilename(filename)
                .build()
        }
    }
}
