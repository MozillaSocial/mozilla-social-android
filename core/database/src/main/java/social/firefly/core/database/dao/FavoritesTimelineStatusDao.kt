package social.firefly.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import social.firefly.core.database.model.entities.statusCollections.FavoritesTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.FavoritesTimelineStatusWrapper

@Dao
interface FavoritesTimelineStatusDao : BaseDao<FavoritesTimelineStatus> {
    @Transaction
    @Query(
        "SELECT * FROM favoritesTimeline " +
                "ORDER BY position ASC",
    )
    fun favoritesTimelinePagingSource(): PagingSource<Int, FavoritesTimelineStatusWrapper>

    @Query("DELETE FROM favoritesTimeline")
    suspend fun deleteFavoritesTimelines()

    @Query(
        "DELETE FROM favoritesTimeline " +
                "WHERE statusId = :statusId"
    )
    suspend fun deletePost(statusId: String)

    @Query(
        "SELECT * FROM favoritesTimeline " +
                "WHERE statusId = :statusId"
    )
    suspend fun getStatus(statusId: String): FavoritesTimelineStatus
}