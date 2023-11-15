package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatusWrapper

@Dao
interface HomeTimelineStatusDao : BaseDao<HomeTimelineStatus> {

    @Transaction
    @Query(
        "SELECT * FROM homeTimeline " +
        "ORDER BY statusId DESC"
    )
    fun homeTimelinePagingSource(): PagingSource<Int, HomeTimelineStatusWrapper>

    @Query("DELETE FROM homeTimeline")
    fun deleteHomeTimeline()

    @Query(
        "DELETE FROM homeTimeline " +
        "WHERE accountId = :accountId " +
        "OR boostedStatusAccountId = :accountId"
    )
    suspend fun removePostsFromAccount(accountId: String)

    @Query(
        "DELETE FROM homeTimeline " +
        "WHERE statusId = :statusId " +
        "OR boostedStatusId = :statusId"
    )
    suspend fun deletePost(statusId: String)

    @Query(
        "SELECT * FROM homeTimeline " +
        "WHERE accountId = :accountId " +
        "OR boostedStatusAccountId = :accountId"
    )
    suspend fun getPostsFromAccount(accountId: String): List<HomeTimelineStatus>
}