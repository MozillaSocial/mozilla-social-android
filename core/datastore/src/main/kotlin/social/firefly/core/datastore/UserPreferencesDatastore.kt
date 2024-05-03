package social.firefly.core.datastore

import androidx.datastore.core.DataStore
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

@OptIn(ExperimentalCoroutinesApi::class)
class UserPreferencesDatastore(
    private val dataStore: DataStore<UserPreferences>,
    val fileName: String,
) {

    private suspend fun preloadData() {
        try {
            dataStore.data.first()
        } catch (ioException: IOException) {
            Timber.e(t = ioException, message = "Problem preloading data")
        }
    }

    init {
        GlobalScope.launch {
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

    suspend fun saveAccessToken(accessToken: String) {
        dataStore.updateData {
            it.toBuilder()
                .setAccessToken(accessToken)
                .build()
        }
    }

    val domain: Flow<String> =
        dataStore.data.mapLatest {
            it.domain
        }

    /**
     * @throws [IllegalArgumentException] if the domain is bad
     */
    suspend fun saveDomain(domain: String) {
        require(HOST_NAME_REGEX.toRegex().matches(domain))
        dataStore.updateData {
            it.toBuilder().setDomain(domain).build()
        }
    }

    val accountId: Flow<String> =
        dataStore.data.mapLatest {
            it.accountId
        }

    suspend fun saveAccountId(accountId: String) {
        dataStore.updateData {
            it.toBuilder()
                .setAccountId(accountId)
                .build()
        }
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
