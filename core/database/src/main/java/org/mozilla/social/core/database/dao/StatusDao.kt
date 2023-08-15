package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import org.mozilla.social.core.database.model.DatabaseStatus

@Dao
interface StatusDao : BaseDao<DatabaseStatus> {
    @Query("SELECT * FROM statuses WHERE statusId IN (:statusIds)")
    fun getAllByIds(statusIds: IntArray): List<DatabaseStatus>

    @Query("SELECT * FROM statuses WHERE isInHomeFeed")
    fun homeTimelinePagingSource(): PagingSource<Int, DatabaseStatus>

    @Query("DELETE FROM statuses WHERE isInHomeFeed")
    fun deleteHomeTimeline()
}