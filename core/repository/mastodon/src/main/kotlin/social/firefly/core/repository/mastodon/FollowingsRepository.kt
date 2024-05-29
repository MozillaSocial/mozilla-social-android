package social.firefly.core.repository.mastodon

import androidx.paging.PagingSource
import social.firefly.core.database.dao.FollowingsDao
import social.firefly.core.database.model.entities.accountCollections.Followee
import social.firefly.core.database.model.entities.accountCollections.FolloweeWrapper

class FollowingsRepository(
    private val dao: FollowingsDao,
) {

    fun getPagingSource(accountId: String): PagingSource<Int, FolloweeWrapper> =
        dao.followingsPagingSource(accountId)

    suspend fun deleteFollowings(accountId: String) = dao.deleteFollowings(accountId)

    suspend fun deleteAllFollowings(
        accountsToKeep: List<String> = emptyList()
    ) = dao.deleteAllFollowings(
        accountsToKeep
    )

    suspend fun insertAll(followees: List<Followee>) = dao.upsertAll(followees)
}
