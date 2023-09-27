package org.mozilla.social.core.data.repository

import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.data.repository.model.account.toExternal
import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.network.AccountApi
import org.mozilla.social.model.Account
import org.mozilla.social.model.Relationship
import org.mozilla.social.model.Status

class AccountRepository internal constructor(
    private val accountApi: AccountApi,
    private val socialDatabase: SocialDatabase,
) {

    suspend fun verifyUserCredentials(): Account {
        return accountApi.verifyCredentials().toExternalModel()
    }

    suspend fun getAccount(accountId: String): Account =
        coroutineScope {
            accountApi.getAccount(accountId).toExternalModel()
        }

    suspend fun getAccountRelationships(
        accountIds: List<String>,
    ): List<Relationship> = accountApi.getRelationships(accountIds.toTypedArray()).map { it.toExternal() }

    suspend fun getAccountFollowers(accountId: String): List<Account> =
        coroutineScope {
            accountApi.getAccountFollowers(accountId).map { it.toExternalModel() }
        }

    suspend fun getAccountFollowing(accountId: String): List<Account> =
        coroutineScope {
            accountApi.getAccountFollowing(accountId).map { it.toExternalModel() }
        }

    suspend fun getAccountStatuses(
        accountId: String,
        olderThanId: String? = null,
        immediatelyNewerThanId: String? = null,
        loadSize: Int? = null,
    ): List<Status> =
        accountApi.getAccountStatuses(
            accountId = accountId,
            olderThanId = olderThanId,
            immediatelyNewerThanId = immediatelyNewerThanId,
            limit = loadSize,
        ).map { it.toExternalModel() }

    suspend fun getAccountBookmarks(): List<Status> =
        coroutineScope {
            accountApi.getAccountBookmarks().map { it.toExternalModel() }
        }

    suspend fun getAccountFavourites(): List<Status> =
        coroutineScope {
            accountApi.getAccountFavourites().map { it.toExternalModel() }
        }

    suspend fun followAccount(accountId: String) {
        accountApi.followAccount(accountId)
    }

    suspend fun unfollowAccount(accountId: String) {
        accountApi.unfollowAccount(accountId)
    }

    /**
     * remove posts from any timelines before blocking
     */
    suspend fun blockAccount(accountId: String) {
        try {
            socialDatabase.homeTimelineDao().remotePostsFromAccount(accountId)
            socialDatabase.relationshipsDao().updateBlocked(accountId, true)
            accountApi.blockAccount(accountId)
        } catch (e: Exception) {
            socialDatabase.relationshipsDao().updateBlocked(accountId, false)
            throw e
        }
    }

    suspend fun unblockAccount(accountId: String) {
        try {
            socialDatabase.relationshipsDao().updateBlocked(accountId, false)
            accountApi.unblockAccount(accountId)
        } catch (e: Exception) {
            socialDatabase.relationshipsDao().updateBlocked(accountId, true)
            throw e
        }
    }

    /**
     * remove posts from any timelines before muting
     */
    suspend fun muteAccount(accountId: String) {
        try {
            socialDatabase.homeTimelineDao().remotePostsFromAccount(accountId)
            socialDatabase.relationshipsDao().updateMuted(accountId, true)
            accountApi.muteAccount(accountId)
        } catch (e: Exception) {
            socialDatabase.relationshipsDao().updateMuted(accountId, false)
            throw e
        }
    }

    suspend fun unmuteAccount(accountId: String) {
        try {
            socialDatabase.relationshipsDao().updateMuted(accountId, false)
            accountApi.unmuteAccount(accountId)
        } catch (e: Exception) {
            socialDatabase.relationshipsDao().updateMuted(accountId, true)
            throw e
        }
    }
}