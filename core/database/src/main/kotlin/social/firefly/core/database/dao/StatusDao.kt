package social.firefly.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import social.firefly.core.database.model.entities.DatabaseStatus
import social.firefly.core.database.model.wrappers.StatusWrapper

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
        "SET isBookmarked = :isBookmarked " +
        "WHERE statusId = :statusId",
    )
    suspend fun updateBookmarked(
        statusId: String,
        isBookmarked: Boolean,
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

    @Query(
        "DELETE FROM statuses " +
                "WHERE statusId NOT IN (:statusIdsToKeep)"
    )
    suspend fun deleteAll(
        statusIdsToKeep: List<String> = emptyList()
    )

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

    @Query(
        "DELETE FROM statuses " +
                "WHERE statusId NOT IN " +
                "( " +
                "SELECT statusId FROM statuses " +
                "WHERE statusId IN " +
                "(" +
                "SELECT statusId FROM favoritesTimeline " +
                "UNION " +
                "SELECT statusId FROM homeTimeline " +
                "UNION " +
                "SELECT statusId FROM localTimeline " +
                "UNION " +
                "SELECT statusId FROM hashTagTimeline " +
                "UNION " +
                "SELECT statusId FROM federatedTimeline " +
                "UNION " +
                "SELECT statusId FROM accountTimeline " +
                "UNION " +
                "SELECT statusId FROM notifications" +
                ") " +
                "UNION " +
                "SELECT boostedStatusId FROM statuses " +
                "WHERE statusId IN " +
                "(" +
                "SELECT statusId FROM favoritesTimeline " +
                "UNION " +
                "SELECT statusId FROM homeTimeline " +
                "UNION " +
                "SELECT statusId FROM localTimeline " +
                "UNION " +
                "SELECT statusId FROM hashTagTimeline " +
                "UNION " +
                "SELECT statusId FROM federatedTimeline " +
                "UNION " +
                "SELECT statusId FROM accountTimeline " +
                "UNION " +
                "SELECT statusId FROM notifications" +
                ") " +
                ")"
    )
    suspend fun deleteOldStatuses()
}
