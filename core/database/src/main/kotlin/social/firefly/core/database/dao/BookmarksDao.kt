package social.firefly.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import social.firefly.core.database.model.entities.statusCollections.BookmarksTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.BookmarksTimelineStatusWrapper

@Dao
interface BookmarksDao : BaseDao<BookmarksTimelineStatus> {
    @Transaction
    @Query(
        "SELECT * FROM bookmarksTimeline " +
        "ORDER BY position ASC",
    )
    fun bookmarksTimelinePagingSource(): PagingSource<Int, BookmarksTimelineStatusWrapper>

    @Query("DELETE FROM bookmarksTimeline")
    suspend fun deleteBookmarksTimelines()

    @Query(
        "DELETE FROM bookmarksTimeline " +
        "WHERE statusId = :statusId"
    )
    suspend fun deletePost(statusId: String)

    @Query(
        "SELECT * FROM bookmarksTimeline " +
        "WHERE statusId = :statusId"
    )
    suspend fun getStatus(statusId: String): BookmarksTimelineStatus
}