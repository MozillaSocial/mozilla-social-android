package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import org.mozilla.social.core.database.model.accountCollections.FollowerWrapper
import org.mozilla.social.core.database.model.accountCollections.Follower

@Dao
interface FollowersDao : BaseDao<Follower> {

    @Query(
        "SELECT * FROM followers " +
        "WHERE  accountId = :accountId " +
        "ORDER BY accountId DESC"
    )
    fun followersPagingSource(
        accountId: String,
    ): PagingSource<Int, FollowerWrapper>

    @Query(
        "DELETE FROM followers " +
        "WHERE accountId = :accountId"
    )
    suspend fun deleteFollowers(accountId: String)
}