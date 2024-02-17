package social.firefly.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import social.firefly.common.getNext
import social.firefly.common.getPrev
import social.firefly.common.toHeaderLink
import social.firefly.core.database.dao.MutesDao
import social.firefly.core.database.model.entities.accountCollections.DatabaseMute
import social.firefly.core.database.model.entities.accountCollections.MuteWrapper
import social.firefly.core.model.MutedUser
import social.firefly.core.model.paging.AccountPagingWrapper
import social.firefly.core.network.mastodon.MutesApi
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import retrofit2.HttpException

class MutesRepository(private val api: MutesApi, private val dao: MutesDao) {
    suspend fun getMutes(
        maxId: String? = null,
        sinceId: String? = null,
        minId: String? = null,
        limit: Int? = null,
    ): AccountPagingWrapper {
        val response = api.getMutes(
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

private fun MuteWrapper.toMutedUser() = MutedUser(
    isMuted = databaseRelationship.isMuting,
    account = account.toExternalModel(),
)
