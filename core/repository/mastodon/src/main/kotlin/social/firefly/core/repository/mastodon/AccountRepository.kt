package social.firefly.core.repository.mastodon

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import social.firefly.common.annotations.PreferUseCase
import social.firefly.core.database.dao.AccountsDao
import social.firefly.core.model.Account
import social.firefly.core.model.Relationship
import social.firefly.core.model.Status
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.network.mastodon.AccountApi
import social.firefly.core.network.mastodon.exceptions.HttpException
import social.firefly.core.network.mastodon.model.isSuccessful
import social.firefly.core.network.mastodon.utils.toMastodonPagedResponse
import social.firefly.core.repository.mastodon.exceptions.AccountNotFoundException
import social.firefly.core.repository.mastodon.model.account.toExternal
import social.firefly.core.repository.mastodon.model.status.toDatabaseModel
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import java.io.File

class AccountRepository internal constructor(
    private val api: AccountApi,
    private val dao: AccountsDao,
) {

    @Suppress("MagicNumber")
    suspend fun getAccount(accountId: String): Account {
        val response = api.getAccount(accountId)
        if (response.code == 404) {
            throw AccountNotFoundException()
        }
        return response.body.toExternalModel()
    }

    fun getAccountFlow(accountId: String): Flow<Account> =
        dao.getAccountFlow(accountId).map { it.toExternalModel() }

    suspend fun getAccountFollowers(
        accountId: String,
        olderThanId: String? = null,
        newerThanId: String? = null,
        loadSize: Int? = null,
    ): MastodonPagedResponse<Account> {
        val response =
            api.getAccountFollowers(
                accountId = accountId,
                olderThanId = olderThanId,
                newerThanId = newerThanId,
                limit = loadSize,
            )
        if (!response.isSuccessful()) {
            throw HttpException(response.body.toString())
        }
        return response.toMastodonPagedResponse { it.toExternalModel() }
    }

    suspend fun getAccountFollowing(
        accountId: String,
        olderThanId: String? = null,
        newerThanId: String? = null,
        loadSize: Int? = null,
    ): MastodonPagedResponse<Account> {
        val response =
            api.getAccountFollowing(
                accountId = accountId,
                olderThanId = olderThanId,
                newerThanId = newerThanId,
                limit = loadSize,
            )
        if (!response.isSuccessful()) {
            throw HttpException(response.body.toString())
        }
        return response.toMastodonPagedResponse { it.toExternalModel() }
    }

    suspend fun getAccountStatuses(
        accountId: String,
        olderThanId: String? = null,
        immediatelyNewerThanId: String? = null,
        loadSize: Int? = null,
        onlyMedia: Boolean = false,
        excludeReplies: Boolean = false,
        excludeBoosts: Boolean = false,
    ): MastodonPagedResponse<Status> {
        val response =
            api.getAccountStatuses(
                accountId = accountId,
                olderThanId = olderThanId,
                immediatelyNewerThanId = immediatelyNewerThanId,
                limit = loadSize,
                onlyMedia = onlyMedia,
                excludeReplies = excludeReplies,
                excludeBoosts = excludeBoosts,
            )

        if (!response.isSuccessful()) {
            throw HttpException(response.body.toString())
        }
        return response.toMastodonPagedResponse { it.toExternalModel() }
    }

    suspend fun getAccountBookmarks(): List<Status> =
        api.getAccountBookmarks().map { it.toExternalModel() }

    suspend fun getAccountFavourites(): List<Status> =
        api.getAccountFavourites().map { it.toExternalModel() }

    @PreferUseCase
    suspend fun followAccount(accountId: String): Relationship =
        api.followAccount(accountId = accountId).toExternal()

    @PreferUseCase
    suspend fun unfollowAccount(accountId: String): Relationship =
        api.unfollowAccount(accountId = accountId).toExternal()

    @PreferUseCase
    suspend fun blockAccount(accountId: String): Relationship =
        api.blockAccount(accountId = accountId).toExternal()

    @PreferUseCase
    suspend fun unblockAccount(accountId: String): Relationship =
        api.unblockAccount(accountId = accountId).toExternal()

    @PreferUseCase
    suspend fun muteAccount(accountId: String): Relationship =
        api.muteAccount(accountId = accountId).toExternal()

    @PreferUseCase
    suspend fun unmuteAccount(accountId: String): Relationship =
        api.unmuteAccount(accountId = accountId).toExternal()

    suspend fun getAccountRelationships(accountIds: List<String>): List<Relationship> =
        api.getRelationships(accountIds).map { it.toExternal() }

    suspend fun getAccountFromDatabase(accountId: String): Account? =
        dao.getAccount(accountId)?.toExternalModel()

    suspend fun deleteAllLocal(
        accountIdsToKeep: List<String> = emptyList()
    ) = dao.deleteAll(accountIdsToKeep)

    suspend fun deleteOldAccountFromDatabase(
        accountIdsToKeep: List<String> = emptyList()
    ) = dao.deleteOldAccounts(accountIdsToKeep)

    @PreferUseCase
    @Suppress("MagicNumber")
    suspend fun updateAccount(
        displayName: String? = null,
        bio: String? = null,
        locked: Boolean? = null,
        bot: Boolean? = null,
        avatar: File? = null,
        header: File? = null,
        fields: List<Pair<String, String>>? = null,
    ): Account = api.updateAccount(
        displayName = displayName,
        bio = bio,
        locked = locked,
        bot = bot,
        avatar = avatar,
        header = header,
        fields = fields,
    ).toExternalModel()

    suspend fun insertAll(accounts: List<Account>) =
        dao.upsertAll(accounts.map { it.toDatabaseModel() })

    suspend fun insert(account: Account) = dao.upsert(account.toDatabaseModel())

    suspend fun updateFollowingCountInDatabase(
        accountId: String,
        valueChange: Long,
    ) = dao.updateFollowingCount(
        accountId = accountId,
        valueChange = valueChange,
    )
}
