package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.mozilla.social.core.database.model.entities.statusCollections.AccountTimelineStatus
import org.mozilla.social.core.database.model.entities.statusCollections.AccountTimelineStatusWrapper
import org.mozilla.social.core.model.AccountTimelineType

@Dao
interface AccountTimelineStatusDao : BaseDao<AccountTimelineStatus> {
    @Transaction
    @Query(
        "SELECT * FROM accountTimeline " +
        "WHERE  accountId = :accountId " +
        "AND timelineType = :timelineType " +
        "ORDER BY statusId DESC",
    )
    fun accountTimelinePagingSource(
        accountId: String,
        timelineType: AccountTimelineType
    ): PagingSource<Int, AccountTimelineStatusWrapper>

    @Query(
        "DELETE FROM accountTimeline " +
        "WHERE accountId = :accountId " +
        "AND timelineType = :timelineType ",
    )
    suspend fun deleteAccountTimeline(
        accountId: String,
        timelineType: AccountTimelineType,
    )

    @Query(
        "DELETE FROM accountTimeline " +
        "WHERE accountId NOT IN (:accountsToKeep)"
    )
    suspend fun deleteAllAccountTimelines(
        accountsToKeep: List<String> = emptyList()
    )
}
