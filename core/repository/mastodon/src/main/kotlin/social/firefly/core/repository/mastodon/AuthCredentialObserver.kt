package social.firefly.core.repository.mastodon

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import social.firefly.core.accounts.AccountsManager
import social.firefly.core.network.mastodon.interceptors.AuthCredentialInterceptor

/**
 * Keeps the domain and access token in [AuthCredentialInterceptor] up to date
 */
@OptIn(DelicateCoroutinesApi::class)
class AuthCredentialObserver(
    authCredentialInterceptor: AuthCredentialInterceptor,
    accountsManager: AccountsManager,
) {
    init {
        GlobalScope.launch {
            coroutineScope {
                accountsManager.getActiveAccountFlow().collectLatest {
                    if (it.accessToken.isNotBlank()) {
                        authCredentialInterceptor.accessToken = it.accessToken
                    }
                }
            }
        }

        GlobalScope.launch {
            accountsManager.getActiveAccountFlow().collectLatest {
                if (it.domain.isNotBlank()) {
                    authCredentialInterceptor.domain = it.domain
                }
            }
        }
    }
}
