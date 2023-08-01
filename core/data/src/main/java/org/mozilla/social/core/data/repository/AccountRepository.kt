package org.mozilla.social.core.data.repository

import org.mozilla.social.core.network.MastodonApi
import org.mozilla.social.model.entity.Account
import retrofit2.Response

class AccountRepository internal constructor(
    private val mastodonApi: MastodonApi
) {

    suspend fun getUserAccount(): Account {
        return mastodonApi.verifyAccount()
    }

    suspend fun getAccount(accountId: String): Response<Account> {
        return mastodonApi.getAccount(accountId)
    }
}