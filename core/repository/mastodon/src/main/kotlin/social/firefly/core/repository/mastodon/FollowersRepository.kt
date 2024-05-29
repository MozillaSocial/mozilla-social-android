package social.firefly.core.repository.mastodon

import androidx.paging.PagingSource
import social.firefly.core.database.dao.FollowersDao
import social.firefly.core.database.model.entities.accountCollections.Follower
import social.firefly.core.database.model.entities.accountCollections.FollowerWrapper

class FollowersRepository(
    private val dao: FollowersDao,
) {
    fun getPagingSource(accountId: String): PagingSource<Int, FollowerWrapper> =
        dao.followersPagingSource(accountId)

    suspend fun deleteFollowers(accountId: String) = dao.deleteFollowers(accountId)

    suspend fun deleteAllFollowers(
        accountsToKeep: List<String> = emptyList()
    ) = dao.deleteAllFollowers(
        accountsToKeep
    )

    suspend fun insertAll(followers: List<Follower>) = dao.upsertAll(followers)
}
