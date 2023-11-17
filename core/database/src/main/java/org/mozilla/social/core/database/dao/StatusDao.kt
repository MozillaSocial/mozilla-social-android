package org.mozilla.social.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.mozilla.social.core.database.model.DatabaseStatus
import org.mozilla.social.core.database.model.wrappers.StatusWrapper

@Dao
interface StatusDao : BaseDao<DatabaseStatus> {
    @Transaction
    @Query(
        "SELECT * FROM statuses " +
            "WHERE statusId = :statusId",
    )
    suspend fun getStatus(statusId: String): StatusWrapper?

    @Transaction
    @Query(
        "SELECT * FROM statuses " +
            "WHERE statusId IN (:statusIds)",
    )
    fun getStatuses(statusIds: List<String>): Flow<List<StatusWrapper>>

    @Query(
        "UPDATE statuses " +
            "SET isBoosted = :isBoosted " +
            "WHERE statusId = :statusId",
    )
    suspend fun updateBoosted(
        statusId: String,
        isBoosted: Boolean,
    )

    @Query(
        "UPDATE statuses " +
            "SET boostsCount = boostsCount + :valueChange " +
            "WHERE statusId = :statusId",
    )
    suspend fun updateBoostCount(
        statusId: String,
        valueChange: Long,
    )

    @Query(
        "UPDATE statuses " +
            "SET isFavorited = :isFavorited " +
            "WHERE statusId = :statusId",
    )
    suspend fun updateFavorited(
        statusId: String,
        isFavorited: Boolean,
    )

    @Query(
        "UPDATE statuses " +
            "SET favouritesCount = favouritesCount + :valueChange " +
            "WHERE statusId = :statusId",
    )
    suspend fun updateFavoriteCount(
        statusId: String,
        valueChange: Long,
    )

    @Query("DELETE FROM statuses")
    fun deleteAll()

    @Query(
        "DELETE FROM statuses " +
            "WHERE statusId = :statusId",
    )
    suspend fun deleteStatus(statusId: String)

    @Query(
        "UPDATE statuses " +
            "SET isBeingDeleted = :isBeingDeleted " +
            "WHERE statusId = :statusId",
    )
    suspend fun updateIsBeingDeleted(
        statusId: String,
        isBeingDeleted: Boolean,
    )
}
