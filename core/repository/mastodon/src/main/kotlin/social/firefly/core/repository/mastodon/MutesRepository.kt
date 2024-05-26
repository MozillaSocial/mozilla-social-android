package social.firefly.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import social.firefly.core.database.dao.MutesDao
import social.firefly.core.database.model.entities.accountCollections.DatabaseMute
import social.firefly.core.database.model.entities.accountCollections.MuteWrapper
import social.firefly.core.model.Account
import social.firefly.core.model.MutedUser
import social.firefly.core.model.paging.MastodonPagedResponse
import social.firefly.core.network.mastodon.MutesApi
import social.firefly.core.network.mastodon.utils.toMastodonPagedResponse
import social.firefly.core.repository.mastodon.model.block.toMutedUser
import social.firefly.core.repository.mastodon.model.status.toExternalModel

class MutesRepository(private val api: MutesApi, private val dao: MutesDao) {
    suspend fun getMutes(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
    ): MastodonPagedResponse<Account> = api.getMutes(
        maxId = maxId,
        sinceId = sinceId,
        minId = minId,
        limit = limit
    ).toMastodonPagedResponse { it.toExternalModel() }

    @OptIn(ExperimentalPagingApi::class)
    fun getMutesPager(
        remoteMediator: RemoteMediator<Int, MuteWrapper>,
        pageSize: Int = 40,
        initialLoadSize: Int = 40,
    ): Flow<PagingData<MutedUser>> {
        return Pager(
            config =
            PagingConfig(
                pageSize = pageSize,
                initialLoadSize = initialLoadSize,
            ),
            remoteMediator = remoteMediator,
        ) {
            dao.pagingSource()
        }.flow.map { it.map { it.toMutedUser() } }
    }

    suspend fun insertAll(databaseAccounts: List<DatabaseMute>) = dao.upsertAll(databaseAccounts)

    suspend fun deleteAll() = dao.deleteAll()
}
