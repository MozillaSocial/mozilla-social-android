package org.mozilla.social.core.datastore

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest

class UserPreferencesDatastore(context: Context) {
    private val dataStore = context.userPreferencesDataStore

    val clientId: Flow<String?> = dataStore.data.mapLatest {
        it.clientId
    }

    val clientSecret: Flow<String?> = dataStore.data.mapLatest {
        it.clientSecret
    }

    val accessToken: Flow<String?> = dataStore.data.mapLatest {
        it.accessToken
    }

    val domain: Flow<String?> = dataStore.data.mapLatest {
        it.domain
    }

    val accountId: Flow<String> = dataStore.data.mapLatest {
        it.accountId
    }

    val isSignedIn: Flow<Boolean> = accessToken.mapLatest {
        !it.isNullOrBlank()
    }.distinctUntilChanged()

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

    suspend fun saveClientCredentials(clientId: String, clientSecret: String) {
        dataStore.updateData {
            it.toBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
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
                .clearClientId()
                .clearClientSecret()
                .build()
        }
    }
}

data class MastodonInstance(
    val clientId: String,
    val clientSecret: String,
)

private val UserPreferences.mastodonInstance: MastodonInstance?
    get() = if (clientId != null && clientSecret != null) {
        MastodonInstance(clientId = clientId, clientSecret = clientSecret)
    } else null
