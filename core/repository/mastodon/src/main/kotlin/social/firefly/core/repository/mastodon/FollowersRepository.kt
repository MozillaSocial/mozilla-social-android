package social.firefly.core.repository.mastodon

import social.firefly.core.database.dao.FollowersDao
import social.firefly.core.database.model.entities.accountCollections.Follower

class FollowersRepository(
    private val dao: FollowersDao,
) {
    fun getPagingSource(accountId: String) = dao.followersPagingSource(accountId)

    suspend fun deleteFollowers(accountId: String) = dao.deleteFollowers(accountId)

    suspend fun deleteAllFollowers(
        accountsToKeep: List<String> = emptyList()
    ) = dao.deleteAllFollowers(
        accountsToKeep
    )

    suspend fun insertAll(followers: List<Follower>) = dao.upsertAll(followers)
}
