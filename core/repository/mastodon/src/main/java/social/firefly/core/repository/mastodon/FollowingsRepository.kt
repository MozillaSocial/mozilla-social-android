package social.firefly.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import social.firefly.core.database.dao.FollowingsDao
import social.firefly.core.database.model.entities.accountCollections.Followee
import social.firefly.core.database.model.entities.accountCollections.FolloweeWrapper
import social.firefly.core.model.wrappers.DetailedAccountWrapper
import social.firefly.core.repository.mastodon.model.account.toDetailedAccount

class FollowingsRepository(
    private val dao: FollowingsDao,
) {
    @ExperimentalPagingApi
    fun getFollowingsPager(
        accountId: String,
        remoteMediator: RemoteMediator<Int, FolloweeWrapper>,
        pageSize: Int = 40,
        initialLoadSize: Int = 40,
    ): Flow<PagingData<DetailedAccountWrapper>> =
        Pager(
            config =
                PagingConfig(
                    pageSize = pageSize,
                    initialLoadSize = initialLoadSize,
                ),
            remoteMediator = remoteMediator,
        ) {
            dao.followingsPagingSource(accountId)
        }.flow.map { pagingData ->
            pagingData.map {
                it.toDetailedAccount()
            }
        }

    suspend fun deleteFollowings(accountId: String) = dao.deleteFollowings(accountId)

    suspend fun deleteAllFollowings(
        accountsToKeep: List<String> = emptyList()
    ) = dao.deleteAllFollowings(
        accountsToKeep
    )

    suspend fun insertAll(followees: List<Followee>) = dao.upsertAll(followees)
}
