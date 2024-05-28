package social.firefly.core.repository.mastodon

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import social.firefly.common.annotations.PreferUseCase
import social.firefly.core.database.dao.AccountsDao
import social.firefly.core.model.Account
import social.firefly.core.model.Relationship
import social.firefly.core.model.Status
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.network.mastodon.AccountApi
import social.firefly.core.network.mastodon.utils.toMastodonPagedResponse
import social.firefly.core.repository.mastodon.model.account.toExternal
import social.firefly.core.repository.mastodon.model.status.toDatabaseModel
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import java.io.File

class AccountRepository internal constructor(
    private val api: AccountApi,
    private val dao: AccountsDao,
) {

    suspend fun getAccount(
        accountId: String,
    ): Account = api.getAccount(accountId).toExternalModel()

    suspend fun getAccounts(
        accountIds: List<String>,
    ) : List<Account> = api.getAccounts(accountIds).map {
        it.toExternalModel()
    }

    fun getAccountFlow(accountId: String): Flow<Account> =
        dao.getAccountFlow(accountId).map { it.toExternalModel() }

    suspend fun getAccountFollowers(
        accountId: String,
        maxId: String? = null,
        sinceId: String? = null,
        loadSize: Int? = null,
    ): MastodonPagedResponse<Account> = api.getAccountFollowers(
        accountId = accountId,
        maxId = maxId,
        sinceId = sinceId,
        limit = loadSize,
    ).toMastodonPagedResponse { it.toExternalModel() }

    suspend fun getAccountFollowing(
        accountId: String,
        maxId: String? = null,
        sinceId: String? = null,
        loadSize: Int? = null,
    ): MastodonPagedResponse<Account> = api.getAccountFollowing(
        accountId = accountId,
        maxId = maxId,
        sinceId = sinceId,
        limit = loadSize,
    ).toMastodonPagedResponse { it.toExternalModel() }

    suspend fun getAccountStatuses(
        accountId: String,
        maxId: String? = null,
        minId: String? = null,
        loadSize: Int? = null,
        onlyMedia: Boolean = false,
        excludeReplies: Boolean = false,
        excludeBoosts: Boolean = false,
    ): MastodonPagedResponse<Status> = api.getAccountStatuses(
        accountId = accountId,
        maxId = maxId,
        minId = minId,
        limit = loadSize,
        onlyMedia = onlyMedia,
        excludeReplies = excludeReplies,
        excludeBoosts = excludeBoosts,
    ).toMastodonPagedResponse { it.toExternalModel() }

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
