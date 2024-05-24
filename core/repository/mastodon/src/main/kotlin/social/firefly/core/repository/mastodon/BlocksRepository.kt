package social.firefly.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import social.firefly.common.getNext
import social.firefly.common.getPrev
import social.firefly.common.toHeaderLink
import social.firefly.core.database.dao.BlocksDao
import social.firefly.core.database.model.entities.accountCollections.BlockWrapper
import social.firefly.core.database.model.entities.accountCollections.DatabaseBlock
import social.firefly.core.model.BlockedUser
import social.firefly.core.model.paging.AccountPagingWrapper
import social.firefly.core.network.mastodon.BlocksApi
import social.firefly.core.repository.mastodon.model.block.toAccountsList
import social.firefly.core.repository.mastodon.model.block.toBlockedUser
import social.firefly.core.repository.mastodon.model.block.toPagingLinks

class BlocksRepository(private val api: BlocksApi, private val dao: BlocksDao) {

    @OptIn(ExperimentalPagingApi::class)
    fun getBlocksPager(
        remoteMediator: RemoteMediator<Int, BlockWrapper>,
        pageSize: Int = 40,
        initialLoadSize: Int = 40,
    ): Flow<PagingData<BlockedUser>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            initialLoadSize = initialLoadSize,
        ),
        remoteMediator = remoteMediator,
    ) {
        dao.pagingSource()
    }.flow.map { pagingData ->
        pagingData.map{
            it.toBlockedUser()
        }
    }


    suspend fun getBlocks(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
    ): AccountPagingWrapper {
        val response = api.getBlocks(
            maxId = maxId,
            sinceId = sinceId,
            minId = minId,
            limit = limit
        )

        if (!response.isSuccessful) {
            throw HttpException(response)
        }

        val pagingLinks = response.toPagingLinks()

        return AccountPagingWrapper(
            accounts = response.toAccountsList(),
            nextPage = pagingLinks?.getNext()?.toHeaderLink(),
            prevPage = pagingLinks?.getPrev()?.toHeaderLink(),
        )
    }

    suspend fun insertAll(databaseAccounts: List<DatabaseBlock>) = dao.upsertAll(databaseAccounts)

    suspend fun deleteAll() = dao.deleteAll()
}
