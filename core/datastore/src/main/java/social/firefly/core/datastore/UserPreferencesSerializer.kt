package social.firefly.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import java.io.InputStream
import java.io.OutputStream

internal object UserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.newBuilder()
        .setThreadType(UserPreferences.ThreadType.LIST)
        .build()

    override suspend fun readFrom(input: InputStream): UserPreferences =
        UserPreferences.parseFrom(input)

    override suspend fun writeTo(
        t: UserPreferences,
        output: OutputStream,
    ) = t.writeTo(output)
}

internal val Context.userPreferencesDataStore: DataStore<UserPreferences> by dataStore(
    fileName = "userPreferences.pb",
    serializer = UserPreferencesSerializer,
)
