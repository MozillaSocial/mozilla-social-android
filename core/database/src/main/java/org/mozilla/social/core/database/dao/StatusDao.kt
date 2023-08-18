package org.mozilla.social.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import org.mozilla.social.core.database.model.DatabaseStatus

@Dao
interface StatusDao : BaseDao<DatabaseStatus> {

    @Query(
        "UPDATE statuses " +
        "SET isBoosted = :isBoosted " +
        "WHERE statusID = :statusId"
    )
    suspend fun updateBoosted(statusId: String, isBoosted: Boolean)

    @Query(
        "UPDATE statuses " +
        "SET boostsCount = boostsCount + :valueChange " +
        "WHERE statusID = :statusId"
    )
    suspend fun updateBoostCount(statusId: String, valueChange: Long)

    @Query("DELETE FROM statuses")
    fun deleteAll()
}
