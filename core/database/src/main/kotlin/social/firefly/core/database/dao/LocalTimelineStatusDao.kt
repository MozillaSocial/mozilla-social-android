package social.firefly.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import social.firefly.core.database.model.entities.statusCollections.LocalTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.LocalTimelineStatusWrapper

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

    @Query(
        "DELETE FROM localTimeline " +
                "WHERE statusId IN " +
                "(" +
                "SELECT statusId FROM statuses " +
                "WHERE accountId IN " +
                "(" +
                "SELECT accountId FROM accounts " +
                "WHERE acct LIKE '%' || :domain || '%' " +
                ")" +
                ")"
    )
    suspend fun removePostsFromDomain(
        domain: String
    )
}
