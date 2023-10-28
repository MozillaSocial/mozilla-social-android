package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import org.mozilla.social.core.database.model.statusCollections.AccountTimelineStatus
import org.mozilla.social.core.database.model.statusCollections.AccountTimelineStatusWrapper

@Dao
interface AccountTimelineStatusDao : BaseDao<AccountTimelineStatus> {

    @Query(
        "SELECT * FROM accountTimeline " +
                "WHERE  accountId = :accountId " +
                "ORDER BY statusId DESC"
    )
    fun accountTimelinePagingSource(
        accountId: String,
    ): PagingSource<Int, AccountTimelineStatusWrapper>

    @Query(
        "DELETE FROM accountTimeline " +
                "WHERE accountId = :accountId "
    )
    suspend fun deleteAccountTimeline(accountId: String)

    @Query("DELETE FROM accountTimeline")
    fun deleteAllAccountTimelines()
}