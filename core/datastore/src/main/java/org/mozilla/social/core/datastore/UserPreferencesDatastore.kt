package org.mozilla.social.core.datastore

import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class UserPreferencesDatastore(context: Context) {
    private val dataStore = context.userPreferencesDataStore

    val accessToken: Flow<String?> =
        dataStore.data.mapLatest {
            it.accessToken
        }

    val domain: Flow<String?> =
        dataStore.data.mapLatest {
            it.domain
        }

    val accountId: Flow<String> =
        dataStore.data.mapLatest {
            it.accountId
        }

    val isSignedIn: Flow<Boolean> =
        dataStore.data.mapLatest {
            !it.accountId.isNullOrBlank() && !it.accessToken.isNullOrBlank()
        }.distinctUntilChanged()

    /**
     * Preload the data so that it's available in the cache
     */
    suspend fun preloadData() {
        try {
            dataStore.data.first()
        } catch (ioException: IOException) {
            Timber.e(t = ioException, message = "Problem preloading data")
        }
    }

    suspend fun saveAccessToken(accessToken: String) {
        dataStore.updateData {
            it.toBuilder()
                .setAccessToken(accessToken)
                .build()
        }
    }

    suspend fun saveAccountId(accountId: String) {
        dataStore.updateData {
            it.toBuilder()
                .setAccountId(accountId)
                .build()
        }
    }

    suspend fun saveDomain(domain: String) {
        dataStore.updateData {
            it.toBuilder().setDomain(domain).build()
        }
    }

    suspend fun clearData() {
        dataStore.updateData {
            it.toBuilder()
                .clearAccessToken()
                .clearDomain()
                .clearAccountId()
                .build()
        }
    }
}
