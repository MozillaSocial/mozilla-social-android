package social.firefly.core.repository.mastodon

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import social.firefly.core.datastore.UserPreferencesDatastoreManager
import social.firefly.core.network.mastodon.interceptors.AuthCredentialInterceptor

/**
 * Keeps the domain and access token in [AuthCredentialInterceptor] up to date
 */
@OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
class AuthCredentialObserver(
    userPreferencesDatastoreManager: UserPreferencesDatastoreManager,
    authCredentialInterceptor: AuthCredentialInterceptor,
) {
    init {
        GlobalScope.launch {
            coroutineScope {
                userPreferencesDatastoreManager.activeUserDatastore.flatMapLatest {
                    it.accessToken
                }.filterNotNull().collectLatest {
                    if (it.isNotBlank()) {
                        authCredentialInterceptor.accessToken = it
                    }
                }
            }
        }

        GlobalScope.launch {
            userPreferencesDatastoreManager.activeUserDatastore.flatMapLatest {
                it.domain
            }.filterNotNull().collectLatest {
                if (it.isNotBlank()) {
                    authCredentialInterceptor.domain = it
                }
            }
        }
    }
}
