package org.mozilla.social.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import org.mozilla.social.common.parseMastodonLinkHeader
import org.mozilla.social.core.database.dao.BlocksDao
import org.mozilla.social.core.database.model.entities.accountCollections.BlockWrapper
import org.mozilla.social.core.database.model.entities.accountCollections.DatabaseBlock
import org.mozilla.social.core.model.BlockedUser
import org.mozilla.social.core.model.paging.AccountPagingWrapper
import org.mozilla.social.core.network.mastodon.BlocksApi
import org.mozilla.social.core.network.mastodon.model.NetworkAccount
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel
import retrofit2.HttpException
import retrofit2.Response

class BlocksRepository(private val api: BlocksApi, private val dao: BlocksDao) {

    @OptIn(ExperimentalPagingApi::class)
    fun getBlocksPager(
        remoteMediator: RemoteMediator<Int, BlockWrapper>,
        pageSize: Int = 40,
        initialLoadSize: Int = 40,
    ): Flow<PagingData<BlockedUser>> {
        return Pager(
            config =
            PagingConfig(
                pageSize = pageSize,
                initialLoadSize = initialLoadSize,
            ),
            remoteMediator = remoteMediator,
        ) {
            dao.pagingSource()
        }.flow.mapLatest { it.map { it.toBlockedUser() } }
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

        return AccountPagingWrapper(
            accounts = response.toAccountsList(),
            pagingLinks = response.toPagingLinks(),
        )
    }

    fun insertAll(databaseAccounts: List<DatabaseBlock>) = dao.upsertAll(databaseAccounts)
}

private fun BlockWrapper.toBlockedUser() = BlockedUser(
    isBlocked = databaseRelationship.isBlocking,
    account = account.toExternalModel(),
)

fun Response<List<NetworkAccount>>.toAccountsList() =
    body()?.map { it.toExternalModel() } ?: emptyList()

fun Response<List<NetworkAccount>>.toPagingLinks() =
    headers().get("link")?.parseMastodonLinkHeader()

