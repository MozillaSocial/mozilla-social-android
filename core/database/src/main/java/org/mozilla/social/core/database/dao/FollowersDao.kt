package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import org.mozilla.social.core.database.model.accountCollections.FollowerWrapper
import org.mozilla.social.core.database.model.accountCollections.Followers

@Dao
interface FollowersDao : BaseDao<Followers> {

    @Query(
        "SELECT * FROM followers " +
        "WHERE  accountId = :accountId " +
        "ORDER BY accountId DESC"
    )
    fun followersPagingSource(
        accountId: String,
    ): PagingSource<Int, FollowerWrapper>
}