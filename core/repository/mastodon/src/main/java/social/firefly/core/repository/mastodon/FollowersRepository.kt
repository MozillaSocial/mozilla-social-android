package social.firefly.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import social.firefly.core.database.dao.FollowersDao
import social.firefly.core.database.model.entities.accountCollections.Follower
import social.firefly.core.database.model.entities.accountCollections.FollowerWrapper
import social.firefly.core.model.wrappers.DetailedAccountWrapper
import social.firefly.core.repository.mastodon.model.account.toDetailedAccount

class FollowersRepository(
    private val dao: FollowersDao,
) {
    @ExperimentalPagingApi
    fun getFollowersPager(
        accountId: String,
        remoteMediator: RemoteMediator<Int, FollowerWrapper>,
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
            dao.followersPagingSource(accountId)
        }.flow.map { pagingData ->
            pagingData.map {
                it.toDetailedAccount()
            }
        }

    suspend fun deleteFollowers(accountId: String) = dao.deleteFollowers(accountId)

    suspend fun deleteAllFollowers(
        accountsToKeep: List<String> = emptyList()
    ) = dao.deleteAllFollowers(
        accountsToKeep
    )

    suspend fun insertAll(followers: List<Follower>) = dao.upsertAll(followers)
}
