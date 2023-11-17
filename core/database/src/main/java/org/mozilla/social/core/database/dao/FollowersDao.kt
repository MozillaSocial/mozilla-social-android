package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.mozilla.social.core.database.model.accountCollections.FollowerWrapper
import org.mozilla.social.core.database.model.accountCollections.Follower

@Dao
interface FollowersDao : BaseDao<Follower> {

    @Transaction
    @Query(
        "SELECT * FROM followers " +
        "WHERE  accountId = :accountId " +
        "ORDER BY position ASC"
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