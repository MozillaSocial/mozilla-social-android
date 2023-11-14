package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import org.mozilla.social.core.database.model.accountCollections.Followings
import org.mozilla.social.core.database.model.accountCollections.FollowingsWrapper

@Dao
interface FollowingsDao : BaseDao<Followings> {

    @Query(
        "SELECT * FROM followings " +
        "WHERE  accountId = :accountId " +
        "ORDER BY accountId DESC"
    )
    fun followingsPagingSource(
        accountId: String,
    ): PagingSource<Int, FollowingsWrapper>
}