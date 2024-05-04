package social.firefly.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import social.firefly.core.datastore.UserPreferences.ThreadType
import social.firefly.core.datastore.UserPreferencesDatastoreManager.Companion.DUMMY_FILENAME
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class UserPreferencesDatastore internal constructor(
    val fileName: String,
    serializer: Serializer<UserPreferences>,
    context: Context,
) {
    private val Context.dataStore: DataStore<UserPreferences> by dataStore(
        fileName = fileName,
        serializer = serializer
    )

    private val dataStore = context.dataStore

    private suspend fun preloadData() {
        try {
            val data = dataStore.data.first()
            println(data)
        } catch (ioException: IOException) {
            Timber.e(t = ioException, message = "Problem preloading data")
        }
    }

    init {
        GlobalScope.launch {
            if (fileName != DUMMY_FILENAME) {
                // This forces a write to disk.  Just creating the datastore with default values
                // does not cause the data store to persist to disk.  A filed must be updated.
                dataStore.updateData {
                    it.toBuilder().setDummyValue(true).build()
                }
            }
            preloadData()
        }
    }

    val isSignedIn: Flow<Boolean> =
        dataStore.data.mapLatest {
            !it.accountId.isNullOrBlank() && !it.accessToken.isNullOrBlank()
        }.distinctUntilChanged()

    suspend fun clearData() {
        dataStore.updateData {
            it.toBuilder()
                .clear()
                .build()
        }
    }

    val accessToken: Flow<String?> =
        dataStore.data.mapLatest {
            it.accessToken
        }

    val domain: Flow<String> =
        dataStore.data.mapLatest {
            it.domain
        }

    val accountId: Flow<String> =
        dataStore.data.mapLatest {
            it.accountId
        }

    val serializedPushKeys: Flow<String> =
        dataStore.data.mapLatest {
            it.serializedPushKeys
        }.distinctUntilChanged()

    suspend fun saveSerializedPushKeyPair(serializedPushKeyPair: String) {
        dataStore.updateData {
            it.toBuilder()
                .setSerializedPushKeys(serializedPushKeyPair)
                .build()
        }
    }

    val lastSeenHomeStatusId: Flow<String> =
        dataStore.data.mapLatest {
            it.lastSeenHomeStatusId
        }.distinctUntilChanged()

    suspend fun saveLastSeenHomeStatusId(statusId: String) {
        dataStore.updateData {
            it.toBuilder()
                .setLastSeenHomeStatusId(statusId)
                .build()
        }
    }

    val threadType: Flow<ThreadType> =
        dataStore.data.mapLatest {
            it.threadType
        }.distinctUntilChanged()

    suspend fun saveThreadType(threadType: ThreadType) {
        dataStore.updateData {
            it.toBuilder()
                .setThreadType(threadType)
                .build()
        }
    }

    companion object {
        @Suppress("MaxLineLength")
        const val HOST_NAME_REGEX = "[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+"
    }
}
