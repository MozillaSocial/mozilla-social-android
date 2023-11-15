package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.mozilla.social.core.database.model.accountCollections.Followee
import org.mozilla.social.core.database.model.accountCollections.FolloweeWrapper

@Dao
interface FollowingsDao : BaseDao<Followee> {

    @Transaction
    @Query(
        "SELECT * FROM followings " +
        "WHERE  accountId = :accountId " +
        "ORDER BY accountId DESC"
    )
    fun followingsPagingSource(
        accountId: String,
    ): PagingSource<Int, FolloweeWrapper>

    @Query(
        "DELETE FROM followings " +
        "WHERE accountId = :accountId"
    )
    suspend fun deleteFollowings(accountId: String)
}