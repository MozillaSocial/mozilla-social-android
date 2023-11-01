package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import org.mozilla.social.core.database.model.statusCollections.FederatedTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.FederatedTimelineStatusWrapper

@Dao
interface FederatedTimelineStatusDao : BaseDao<FederatedTimelineStatus> {
    @Query(
        "SELECT * FROM federatedTimeline " +
        "ORDER BY statusId DESC"
    )
    fun federatedTimelinePagingSource(): PagingSource<Int, FederatedTimelineStatusWrapper>

    @Query("DELETE FROM federatedTimeline")
    fun deleteFederatedTimeline()

    @Query(
        "DELETE FROM federatedTimeline " +
        "WHERE accountId = :accountId " +
        "OR boostedStatusAccountId = :accountId"
    )
    suspend fun removePostsFromAccount(accountId: String)

    @Query(
        "DELETE FROM federatedTimeline " +
        "WHERE statusId = :statusId " +
        "OR boostedStatusId = :statusId"
    )
    suspend fun deletePost(statusId: String)
}