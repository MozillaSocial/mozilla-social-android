package social.firefly.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import social.firefly.core.database.model.entities.statusCollections.HomeTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.HomeTimelineStatusWrapper

@Dao
interface HomeTimelineStatusDao : BaseDao<HomeTimelineStatus> {
    @Transaction
    @Query(
        "SELECT * FROM homeTimeline " +
        "ORDER BY statusId DESC",
    )
    fun homeTimelinePagingSource(): PagingSource<Int, HomeTimelineStatusWrapper>

    @Query("DELETE FROM homeTimeline")
    suspend fun deleteHomeTimeline()

    @Query(
        "DELETE FROM homeTimeline " +
        "WHERE statusId IN " +
        "(" +
            "SELECT statusId FROM statuses " +
            "WHERE accountId = :accountId " +
            "OR boostedStatusAccountId = :accountId" +
        ")",
    )
    suspend fun removePostsFromAccount(accountId: String)

    @Transaction
    @Query(
        "SELECT * FROM homeTimeline " +
        "WHERE statusId IN " +
        "(" +
            "SELECT statusId FROM statuses " +
            "WHERE accountId = :accountId " +
            "OR boostedStatusAccountId = :accountId" +
        ")",
    )
    suspend fun getPostsFromAccount(accountId: String): List<HomeTimelineStatusWrapper>
}
