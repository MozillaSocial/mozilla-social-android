package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.mozilla.social.core.database.model.statusCollections.HashTagTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.HashTagTimelineStatusWrapper

@Dao
interface HashTagTimelineStatusDao : BaseDao<HashTagTimelineStatus> {

    @Transaction
    @Query(
        "SELECT * FROM hashTagTimeline " +
        "WHERE  hashTag = :hashTag " +
        "ORDER BY statusId DESC"
    )
    fun hashTagTimelinePagingSource(
        hashTag: String,
    ): PagingSource<Int, HashTagTimelineStatusWrapper>

    @Query(
        "DELETE FROM hashTagTimeline " +
        "WHERE hashTag = :hashTag "
    )
    suspend fun deleteHashTagTimeline(hashTag: String)

    @Query("DELETE FROM hashTagTimeline")
    fun deleteAllHashTagTimelines()

    @Query(
        "DELETE FROM hashTagTimeline " +
        "WHERE statusId = :statusId " +
        "OR boostedStatusId = :statusId"
    )
    suspend fun deletePost(statusId: String)
}