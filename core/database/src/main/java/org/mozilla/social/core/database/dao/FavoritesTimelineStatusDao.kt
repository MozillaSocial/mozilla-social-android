package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.mozilla.social.core.database.model.statusCollections.FavoritesTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.FavoritesTimelineStatusWrapper

@Dao
interface FavoritesTimelineStatusDao: BaseDao<FavoritesTimelineStatus> {
    @Transaction
    @Query(
        "SELECT * FROM favoritesTimeline " +
        "ORDER BY position ASC",
    )
    fun favoritesTimelinePagingSource(): PagingSource<Int, FavoritesTimelineStatusWrapper>

    @Query("DELETE FROM favoritesTimeline")
    fun deleteFavoritesTimelines()

    @Query(
        "DELETE FROM favoritesTimeline " +
        "WHERE statusId = :statusId " +
        "OR boostedStatusId = :statusId",
    )
    suspend fun deletePost(statusId: String)
}