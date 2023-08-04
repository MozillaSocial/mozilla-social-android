package org.mozilla.social.core.data.repository

import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.network.MastodonApi
import org.mozilla.social.core.network.model.NetworkAccount
import org.mozilla.social.core.network.model.NetworkStatus
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

    suspend fun getAccountFollowers(accountId: String): List<NetworkAccount> =
        coroutineScope {
            mastodonApi.getAccountFollowers(accountId)
        }

    suspend fun getAccountFollowing(accountId: String): List<NetworkAccount> =
        coroutineScope {
            mastodonApi.getAccountFollowing(accountId)
        }

    suspend fun getAccountStatuses(accountId: String): List<NetworkStatus> =
        coroutineScope {
            mastodonApi.getAccountStatuses(accountId)
        }

    suspend fun getAccountBookmarks(): List<NetworkStatus> =
        coroutineScope {
            mastodonApi.getAccountBookmarks()
        }

    suspend fun getAccountFavourites(): List<NetworkStatus> =
        coroutineScope {
            mastodonApi.getAccountFavourites()
        }
}