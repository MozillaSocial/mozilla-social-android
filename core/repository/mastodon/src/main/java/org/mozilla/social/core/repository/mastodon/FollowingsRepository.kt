package org.mozilla.social.core.repository.mastodon

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mozilla.social.core.database.dao.FollowingsDao
import org.mozilla.social.core.database.model.entities.accountCollections.Followee
import org.mozilla.social.core.database.model.entities.accountCollections.FolloweeWrapper
import org.mozilla.social.core.model.wrappers.DetailedAccountWrapper
import org.mozilla.social.core.repository.mastodon.model.account.toDetailedAccount

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

    fun insertAll(followees: List<Followee>) = dao.insertAll(followees)
}
