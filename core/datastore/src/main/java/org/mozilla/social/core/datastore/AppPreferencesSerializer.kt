package org.mozilla.social.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import java.io.InputStream
import java.io.OutputStream

internal object AppPreferencesSerializer : Serializer<AppPreferences> {
    override val defaultValue: AppPreferences =
        AppPreferences.getDefaultInstance().toBuilder().setTrackAnalytics(true).build()


    override suspend fun readFrom(input: InputStream): AppPreferences = AppPreferences.parseFrom(input)

    override suspend fun writeTo(
        t: AppPreferences,
        output: OutputStream,
    ) = t.writeTo(output)
}

internal val Context.appPreferencesDataStore: DataStore<AppPreferences> by dataStore(
    fileName = "appPreferences.pb",
    serializer = AppPreferencesSerializer,
)
