package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import org.mozilla.social.core.database.model.statusCollections.HashTagTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.HomeTimelineStatusWrapper

@Dao
interface HashTagTimelineStatusDao : BaseDao<HashTagTimelineStatus> {

    @Query(
        "SELECT * FROM hashTagTimeline " +
        "WHERE  hashTag = :hashTag " +
        "ORDER BY createdAt DESC"
    )
    fun homeTimelinePagingSource(
        hashTag: String,
    ): PagingSource<Int, HomeTimelineStatusWrapper>

    @Query(
        "DELETE FROM hashTagTimeline " +
        "WHERE hashTag = :hashTag "
    )
    suspend fun deleteHashTagTimeline(hashTag: String)

    @Query("DELETE FROM hashTagTimeline")
    fun deleteAllHashTagTimelines()
}