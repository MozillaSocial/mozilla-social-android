package org.mozilla.social.core.data.repository

import org.mozilla.social.core.data.repository.model.toExternalModel
import org.mozilla.social.core.network.AccountApi
import org.mozilla.social.model.Account

class AccountRepository internal constructor(private val accountApi: AccountApi) {
    suspend fun getAccount(accountId: String): Account =
        accountApi.getAccount(accountId = accountId).toExternalModel()
}