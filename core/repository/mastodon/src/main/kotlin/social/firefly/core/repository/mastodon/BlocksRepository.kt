package social.firefly.core.repository.mastodon

import androidx.paging.PagingSource
import social.firefly.core.database.dao.BlocksDao
import social.firefly.core.database.model.entities.accountCollections.BlockWrapper
import social.firefly.core.database.model.entities.accountCollections.DatabaseBlock
import social.firefly.core.model.Account
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.network.mastodon.BlocksApi
import social.firefly.core.network.mastodon.utils.toMastodonPagedResponse
import social.firefly.core.repository.mastodon.model.status.toExternalModel

class BlocksRepository(private val api: BlocksApi, private val dao: BlocksDao) {

    fun getPagingSource(): PagingSource<Int, BlockWrapper> = dao.pagingSource()

    suspend fun getBlocks(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
    ): MastodonPagedResponse<Account> = api.getBlocks(
        maxId = maxId,
        sinceId = sinceId,
        minId = minId,
        limit = limit
    ).toMastodonPagedResponse { it.toExternalModel() }

    suspend fun insertAll(databaseAccounts: List<DatabaseBlock>) = dao.upsertAll(databaseAccounts)

    suspend fun deleteAll() = dao.deleteAll()
}
