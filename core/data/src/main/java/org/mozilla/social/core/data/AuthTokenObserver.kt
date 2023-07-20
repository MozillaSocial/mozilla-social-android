package org.mozilla.social.core.data

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.mozilla.social.core.datastore.UserPreferencesDatastore
import org.mozilla.social.core.network.interceptors.AuthInterceptor

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class AuthTokenObserver(
    userPreferencesDatastore: UserPreferencesDatastore,
    authInterceptor: AuthInterceptor,
) {

    init {
        GlobalScope.launch {
            userPreferencesDatastore.dataStore.data.mapLatest {
                authInterceptor.accessToken = it.accessToken
            }.collect()
        }
    }
}