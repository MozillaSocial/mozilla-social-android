package org.mozilla.social.core.data.repository

import org.mozilla.social.common.parseMastodonLinkHeader
import org.mozilla.social.core.data.repository.model.account.toExternal
import org.mozilla.social.core.data.repository.model.followers.FollowersPagingWrapper
import org.mozilla.social.core.data.repository.model.status.StatusPagingWrapper
import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.network.mastodon.AccountApi
import org.mozilla.social.model.Account
import org.mozilla.social.model.Relationship
import org.mozilla.social.model.Status
import retrofit2.HttpException

class AccountRepository internal constructor(
    private val accountApi: AccountApi,
    private val socialDatabase: SocialDatabase,
) {

    suspend fun verifyUserCredentials(): Account {
        return accountApi.verifyCredentials().toExternalModel()
    }

    suspend fun getAccount(accountId: String): Account =
        accountApi.getAccount(accountId).toExternalModel()

    suspend fun getAccountFromDatabase(accountId: String): Account? =
        socialDatabase.accountsDao().getAccount(accountId)?.toExternalModel()

    suspend fun getAccountRelationships(
        accountIds: List<String>,
    ): List<Relationship> = accountApi.getRelationships(accountIds.toTypedArray()).map { it.toExternal() }

    suspend fun getAccountFollowers(
        accountId: String,
        olderThanId: String? = null,
        newerThanId: String? = null,
        loadSize: Int? = null,
    ): FollowersPagingWrapper {
        val response = accountApi.getAccountFollowers(
            accountId = accountId,
            olderThanId = olderThanId,
            newerThanId = newerThanId,
            limit = loadSize,
        )
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        return FollowersPagingWrapper(
            accounts = response.body()?.map { it.toExternalModel() } ?: emptyList(),
            link = response.headers().get("link"),
        )
    }

    suspend fun getAccountFollowing(
        accountId: String,
        olderThanId: String? = null,
        newerThanId: String? = null,
        loadSize: Int? = null,
    ): FollowersPagingWrapper {
        val response = accountApi.getAccountFollowing(
            accountId = accountId,
            olderThanId = olderThanId,
            newerThanId = newerThanId,
            limit = loadSize,
        )
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        return FollowersPagingWrapper(
            accounts = response.body()?.map { it.toExternalModel() } ?: emptyList(),
            link = response.headers().get("link"),
        )
    }

    suspend fun getAccountStatuses(
        accountId: String,
        olderThanId: String? = null,
        immediatelyNewerThanId: String? = null,
        loadSize: Int? = null,
        onlyMedia: Boolean = false,
        excludeReplies: Boolean = false,
        excludeBoosts: Boolean = false,
    ): StatusPagingWrapper {
        val response = accountApi.getAccountStatuses(
            accountId = accountId,
            olderThanId = olderThanId,
            immediatelyNewerThanId = immediatelyNewerThanId,
            limit = loadSize,
            onlyMedia = onlyMedia,
            excludeReplies = excludeReplies,
            excludeBoosts = excludeBoosts,
        )

        if (!response.isSuccessful) {
            throw HttpException(response)
        }

        return StatusPagingWrapper(
            statuses = response.body()?.map { it.toExternalModel() } ?: emptyList(),
            pagingLinks = response.headers().get("link")?.parseMastodonLinkHeader(),
        )
    }

    suspend fun getAccountBookmarks(): List<Status> =
        accountApi.getAccountBookmarks().map { it.toExternalModel() }

    suspend fun getAccountFavourites(): List<Status> =
        accountApi.getAccountFavourites().map { it.toExternalModel() }
}