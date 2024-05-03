package social.firefly.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import java.io.InputStream
import java.io.OutputStream

const val APP_PREFERENCES_DATASTORE_FILENAME = "appPreferences.pb"

internal object AppPreferencesSerializer : Serializer<AppPreferences> {
    override val defaultValue: AppPreferences = AppPreferences
        .getDefaultInstance()
        .toBuilder()
        .setTrackAnalytics(true)
        .setThemeType(AppPreferences.ThemeType.SYSTEM)
        .build()


    override suspend fun readFrom(input: InputStream): AppPreferences =
        AppPreferences.parseFrom(input)

    override suspend fun writeTo(
        t: AppPreferences,
        output: OutputStream,
    ) = t.writeTo(output)
}

internal val Context.appPreferencesDataStore: DataStore<AppPreferences> by dataStore(
    fileName = APP_PREFERENCES_DATASTORE_FILENAME,
    serializer = AppPreferencesSerializer,
)
