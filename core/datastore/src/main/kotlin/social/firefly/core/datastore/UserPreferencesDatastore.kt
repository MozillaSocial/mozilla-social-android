package social.firefly.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import social.firefly.core.datastore.UserPreferences.ThreadType
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
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
            preloadData()
        }
    }

    val accessToken: Flow<String> = dataStore.data.mapLatest { it.accessToken }
    val domain: Flow<String> = dataStore.data.mapLatest { it.domain }
    val accountId: Flow<String> = dataStore.data.mapLatest { it.accountId }
    val avatarUrl: Flow<String> = dataStore.data.mapLatest { it.avatarUrl }
    val userName: Flow<String> = dataStore.data.mapLatest { it.userName }
    val serializedPushKeys: Flow<String> = dataStore.data.mapLatest { it.serializedPushKeys }
    val lastSeenHomeStatusId: Flow<String> = dataStore.data.mapLatest { it.lastSeenHomeStatusId }
    val threadType: Flow<ThreadType> = dataStore.data.mapLatest { it.threadType }.distinctUntilChanged()

    suspend fun saveAvatarUrl(url: String) {
        dataStore.updateData {
            it.toBuilder()
                .setAvatarUrl(url)
                .build()
        }
    }

    suspend fun saveUserName(userName: String) {
        dataStore.updateData {
            it.toBuilder()
                .setUserName(userName)
                .build()
        }
    }

    suspend fun saveSerializedPushKeyPair(serializedPushKeyPair: String) {
        dataStore.updateData {
            it.toBuilder()
                .setSerializedPushKeys(serializedPushKeyPair)
                .build()
        }
    }

    suspend fun saveLastSeenHomeStatusId(statusId: String) {
        dataStore.updateData {
            it.toBuilder()
                .setLastSeenHomeStatusId(statusId)
                .build()
        }
    }

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
