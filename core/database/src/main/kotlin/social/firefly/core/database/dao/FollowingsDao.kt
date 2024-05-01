package social.firefly.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import social.firefly.core.database.model.entities.accountCollections.Followee
import social.firefly.core.database.model.entities.accountCollections.FolloweeWrapper

@Dao
interface FollowingsDao : BaseDao<Followee> {
    @Transaction
    @Query(
        "SELECT * FROM followings " +
                "WHERE  accountId = :accountId " +
                "ORDER BY position ASC",
    )
    fun followingsPagingSource(accountId: String): PagingSource<Int, FolloweeWrapper>

    @Query(
        "DELETE FROM followings " +
                "WHERE accountId = :accountId",
    )
    suspend fun deleteFollowings(accountId: String)

    @Query(
        "DELETE FROM followings " +
                "WHERE accountId NOT IN (:accountsToKeep)"
    )
    suspend fun deleteAllFollowings(
        accountsToKeep: List<String> = emptyList()
    )
}
