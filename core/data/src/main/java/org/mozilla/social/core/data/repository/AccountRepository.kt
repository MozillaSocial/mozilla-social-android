package org.mozilla.social.core.data.repository

import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.data.repository.model.toExternalModel
import org.mozilla.social.core.network.AccountApi
import org.mozilla.social.model.Account
import org.mozilla.social.model.Status

class AccountRepository internal constructor(
    private val accountApi: AccountApi
) {

    suspend fun getUserAccount(): Account {
        return accountApi.verifyAccount().toExternalModel()
    }

    suspend fun getAccount(accountId: String): Account =
        coroutineScope {
            accountApi.getAccount(accountId).toExternalModel()
        }

    suspend fun getAccountFollowers(accountId: String): List<Account> =
        coroutineScope {
            accountApi.getAccountFollowers(accountId).map { it.toExternalModel() }
        }

    suspend fun getAccountFollowing(accountId: String): List<Account> =
        coroutineScope {
            accountApi.getAccountFollowing(accountId).map { it.toExternalModel() }
        }

    suspend fun getAccountStatuses(accountId: String): List<Status> =
        coroutineScope {
            accountApi.getAccountStatuses(accountId).map { it.toExternalModel() }
        }

    suspend fun getAccountBookmarks(): List<Status> =
        coroutineScope {
            accountApi.getAccountBookmarks().map { it.toExternalModel() }
        }

    suspend fun getAccountFavourites(): List<Status> =
        coroutineScope {
            accountApi.getAccountFavourites().map { it.toExternalModel() }
        }
}