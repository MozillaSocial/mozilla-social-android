package org.mozilla.social.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import org.mozilla.social.core.database.model.DatabaseStatus

@Dao
interface StatusDao : BaseDao<DatabaseStatus> {
    @Query("SELECT * FROM databasestatus WHERE statusId IN (:statusIds)")
    fun getAllByIds(statusIds: IntArray): List<DatabaseStatus>
}