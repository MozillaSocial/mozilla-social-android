package social.firefly.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import social.firefly.core.database.model.entities.accountCollections.Follower
import social.firefly.core.database.model.entities.accountCollections.FollowerWrapper

@Dao
interface FollowersDao : BaseDao<Follower> {
    @Transaction
    @Query(
        "SELECT * FROM followers " +
                "WHERE  accountId = :accountId " +
                "ORDER BY position ASC",
    )
    fun followersPagingSource(accountId: String): PagingSource<Int, FollowerWrapper>

    @Query(
        "DELETE FROM followers " +
                "WHERE accountId = :accountId",
    )
    suspend fun deleteFollowers(accountId: String)

    @Query(
        "DELETE FROM followers " +
                "WHERE accountId NOT IN (:accountsToKeep)"
    )
    suspend fun deleteAllFollowers(
        accountsToKeep: List<String> = emptyList()
    )
}
