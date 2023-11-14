package org.mozilla.social.core.repository.mastodon

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.mozilla.social.common.parseMastodonLinkHeader
import org.mozilla.social.core.database.dao.AccountsDao
import org.mozilla.social.core.network.mastodon.AccountApi
import org.mozilla.social.core.repository.mastodon.model.account.toExternal
import org.mozilla.social.core.model.paging.FollowersPagingWrapper
import org.mozilla.social.core.model.paging.StatusPagingWrapper
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.model.Relationship
import org.mozilla.social.core.model.Status
import retrofit2.HttpException
import java.io.File

class AccountRepository internal constructor(
    private val api: AccountApi,
    private val dao: AccountsDao,
) {
    suspend fun getAccount(accountId: String): Account {
        return api.getAccount(accountId).toExternalModel()
    }

    suspend fun verifyUserCredentials(): Account {
        return api.verifyCredentials().toExternalModel()
    }

    suspend fun getAccountFollowers(
        accountId: String,
        olderThanId: String? = null,
        newerThanId: String? = null,
        loadSize: Int? = null,
    ): FollowersPagingWrapper {
        val response = api.getAccountFollowers(
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
        val response = api.getAccountFollowing(
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
        val response = api.getAccountStatuses(
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
        api.getAccountBookmarks().map { it.toExternalModel() }

    suspend fun getAccountFavourites(): List<Status> =
        api.getAccountFavourites().map { it.toExternalModel() }

    suspend fun followAccount(accountId: String) = api.followAccount(accountId = accountId)

    suspend fun unfollowAccount(accountId: String) =
        api.unfollowAccount(accountId = accountId)

    suspend fun blockAccount(accountId: String) = api.blockAccount(accountId = accountId)
    suspend fun unblockAccount(accountId: String) = api.unblockAccount(accountId = accountId)


    suspend fun muteAccount(accountId: String) = api.muteAccount(accountId = accountId)
    suspend fun unmuteAccount(accountId: String) = api.unmuteAccount(accountId = accountId)

    suspend fun getAccountRelationships(
        accountIds: List<String>,
    ): List<Relationship> =
        api.getRelationships(accountIds.toTypedArray()).map { it.toExternal() }

    // TODO@DA move to use case
    suspend fun getAccountFromDatabase(accountId: String): Account? =
        dao.getAccount(accountId)?.toExternalModel()

    @Suppress("MagicNumber")
    suspend fun updateAccount(
        displayName: String? = null,
        bio: String? = null,
        locked: Boolean? = null,
        bot: Boolean? = null,
        avatar: File? = null,
        header: File? = null,
        fields: List<Pair<String, String>>? = null
    ) = api.updateAccount(
        displayName = displayName?.toRequestBody(MultipartBody.FORM),
        bio = bio?.toRequestBody(MultipartBody.FORM),
        locked = locked?.toString()?.toRequestBody(MultipartBody.FORM),
        bot = bot?.toString()?.toRequestBody(MultipartBody.FORM),
        avatar = avatar?.let {
            MultipartBody.Part.createFormData(
                "avatar",
                avatar.name,
                avatar.asRequestBody("image/*".toMediaTypeOrNull()),
            )
        },
        header = header?.let {
            MultipartBody.Part.createFormData(
                "header",
                header.name,
                header.asRequestBody("image/*".toMediaTypeOrNull()),
            )
        },
        fieldLabel0 = fields?.getOrNull(0)?.first?.toRequestBody(MultipartBody.FORM),
        fieldContent0 = fields?.getOrNull(0)?.second?.toRequestBody(MultipartBody.FORM),
        fieldLabel1 = fields?.getOrNull(1)?.first?.toRequestBody(MultipartBody.FORM),
        fieldContent1 = fields?.getOrNull(1)?.second?.toRequestBody(MultipartBody.FORM),
        fieldLabel2 = fields?.getOrNull(2)?.first?.toRequestBody(MultipartBody.FORM),
        fieldContent2 = fields?.getOrNull(2)?.second?.toRequestBody(MultipartBody.FORM),
        fieldLabel3 = fields?.getOrNull(3)?.first?.toRequestBody(MultipartBody.FORM),
        fieldContent3 = fields?.getOrNull(3)?.second?.toRequestBody(MultipartBody.FORM),
    ).toExternalModel()
}