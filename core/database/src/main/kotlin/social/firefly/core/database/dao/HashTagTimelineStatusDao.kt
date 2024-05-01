package social.firefly.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import social.firefly.core.database.model.entities.statusCollections.HashTagTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.HashTagTimelineStatusWrapper

@Dao
interface HashTagTimelineStatusDao : BaseDao<HashTagTimelineStatus> {
    @Transaction
    @Query(
        "SELECT * FROM hashTagTimeline " +
                "WHERE  hashTag = :hashTag " +
                "ORDER BY statusId DESC",
    )
    fun hashTagTimelinePagingSource(hashTag: String): PagingSource<Int, HashTagTimelineStatusWrapper>

    @Query(
        "DELETE FROM hashTagTimeline " +
                "WHERE hashTag = :hashTag ",
    )
    suspend fun deleteHashTagTimeline(hashTag: String)

    @Query("DELETE FROM hashTagTimeline")
    suspend fun deleteAllHashTagTimelines()
}
