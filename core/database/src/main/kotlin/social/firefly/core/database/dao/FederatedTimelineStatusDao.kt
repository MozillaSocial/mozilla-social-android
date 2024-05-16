package social.firefly.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import social.firefly.core.database.model.entities.statusCollections.FederatedTimelineStatus
import social.firefly.core.database.model.entities.statusCollections.FederatedTimelineStatusWrapper

@Dao
interface FederatedTimelineStatusDao : BaseDao<FederatedTimelineStatus> {
    @Transaction
    @Query(
        "SELECT * FROM federatedTimeline " +
                "ORDER BY statusId DESC",
    )
    fun federatedTimelinePagingSource(): PagingSource<Int, FederatedTimelineStatusWrapper>

    @Query("DELETE FROM federatedTimeline")
    suspend fun deleteFederatedTimeline()

    @Query(
        "DELETE FROM federatedTimeline " +
                "WHERE statusId IN " +
                "(" +
                "SELECT statusId FROM statuses " +
                "WHERE accountId = :accountId " +
                "OR boostedStatusAccountId = :accountId" +
                ")",
    )
    suspend fun removePostsFromAccount(accountId: String)

    @Query(
        "DELETE FROM federatedTimeline " +
                "WHERE statusId IN " +
                "(" +
                "SELECT statusId FROM statuses " +
                "WHERE accountId IN " +
                "(" +
                "SELECT accountId FROM accounts " +
                "WHERE acct LIKE '%' + :domain + '%' " +
                ")" +
                ")"
    )
    suspend fun removePostsFromDomain(
        domain: String
    )
}
