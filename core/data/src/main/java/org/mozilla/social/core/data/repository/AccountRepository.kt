package org.mozilla.social.core.data.repository

import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.network.MastodonApi
import org.mozilla.social.core.network.model.NetworkAccount
import retrofit2.Response

class AccountRepository internal constructor(
    private val mastodonApi: MastodonApi
) {

    suspend fun getUserAccount(): NetworkAccount {
        return mastodonApi.verifyAccount()
    }

    suspend fun getAccount(accountId: String): NetworkAccount =
        coroutineScope {
            mastodonApi.getAccount(accountId)
        }
}