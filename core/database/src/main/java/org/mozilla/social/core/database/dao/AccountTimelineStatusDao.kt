package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.mozilla.social.core.database.model.statusCollections.AccountTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.AccountTimelineStatusWrapper
import org.mozilla.social.core.model.TimelineType

@Dao
interface AccountTimelineStatusDao : BaseDao<AccountTimelineStatus> {
    @Transaction
    @Query(
        "SELECT * FROM accountTimeline " +
        "WHERE  accountId = :accountId " +
        "AND timelineType = :timelineType " +
        "ORDER BY statusId DESC",
    )
    fun accountTimelinePagingSource(
        accountId: String,
        timelineType: TimelineType
    ): PagingSource<Int, AccountTimelineStatusWrapper>

    @Query(
        "DELETE FROM accountTimeline " +
        "WHERE accountId = :accountId " +
        "AND timelineType = :timelineType ",
    )
    suspend fun deleteAccountTimeline(
        accountId: String,
        timelineType: TimelineType,
    )

    @Query("DELETE FROM accountTimeline")
    fun deleteAllAccountTimelines()

    @Query(
        "DELETE FROM accountTimeline " +
        "WHERE statusId = :statusId " +
        "OR boostedStatusId = :statusId",
    )
    suspend fun deletePost(statusId: String)
}
