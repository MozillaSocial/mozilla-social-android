package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import org.mozilla.social.core.database.model.statusCollections.LocalTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.LocalTimelineStatusWrapper

@Dao
interface LocalTimelineStatusDao : BaseDao<LocalTimelineStatus> {
    @Query(
        "SELECT * FROM localTimeline " +
        "ORDER BY statusId DESC"
    )
    fun localTimelinePagingSource(): PagingSource<Int, LocalTimelineStatusWrapper>

    @Query("DELETE FROM localTimeline")
    fun deleteLocalTimeline()

    @Query(
        "DELETE FROM localTimeline " +
        "WHERE accountId = :accountId " +
        "OR boostedStatusAccountId = :accountId"
    )
    suspend fun removePostsFromAccount(accountId: String)
}