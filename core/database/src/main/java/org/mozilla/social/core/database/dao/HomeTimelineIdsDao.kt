package org.mozilla.social.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import org.mozilla.social.core.database.model.DatabaseHomeTimelineStatusId

@Dao
interface HomeTimelineIdsDao : BaseDao<DatabaseHomeTimelineStatusId> {
    @Query("SELECT * FROM databasehometimelinestatusid")
    fun getAll(): List<DatabaseHomeTimelineStatusId>
}