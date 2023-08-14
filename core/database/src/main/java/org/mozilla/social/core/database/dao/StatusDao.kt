package org.mozilla.social.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import org.mozilla.social.core.database.model.DatabaseStatus

@Dao
interface StatusDao : BaseDao<DatabaseStatus> {
    @Query("SELECT * FROM databasestatus WHERE statusId IN (:statusIds)")
    fun getAllByIds(statusIds: IntArray): List<DatabaseStatus>

    @Query("SELECT * FROM databasestatus WHERE isInHomeFeed")
    fun homeTimelinePagingSource(): PagingSource<String, DatabaseStatus>

    @Query("DELETE FROM databasestatus WHERE isInHomeFeed")
    fun deleteHomeTimeline()
}