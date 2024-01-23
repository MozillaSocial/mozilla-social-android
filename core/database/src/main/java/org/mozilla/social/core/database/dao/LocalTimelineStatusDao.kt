package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.mozilla.social.core.database.model.entities.statusCollections.LocalTimelineStatus
import org.mozilla.social.core.database.model.entities.statusCollections.LocalTimelineStatusWrapper

@Dao
interface LocalTimelineStatusDao : BaseDao<LocalTimelineStatus> {
    @Transaction
    @Query(
        "SELECT * FROM localTimeline " +
        "ORDER BY statusId DESC",
    )
    fun localTimelinePagingSource(): PagingSource<Int, LocalTimelineStatusWrapper>

    @Query("DELETE FROM localTimeline")
    suspend fun deleteLocalTimeline()

    @Query(
        "DELETE FROM localTimeline " +
        "WHERE statusId IN " +
        "(" +
                "SELECT statusId FROM statuses " +
                "WHERE accountId = :accountId " +
                "OR boostedStatusAccountId = :accountId" +
        ")",
    )
    suspend fun removePostsFromAccount(accountId: String)
}
