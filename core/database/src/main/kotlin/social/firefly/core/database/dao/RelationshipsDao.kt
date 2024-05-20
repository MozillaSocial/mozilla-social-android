package social.firefly.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import social.firefly.core.database.model.entities.DatabaseRelationship

@Dao
interface RelationshipsDao : BaseDao<DatabaseRelationship> {
    @Query(
        "SELECT * FROM relationships " +
                "WHERE accountId = :accountId",
    )
    fun getRelationshipFlow(accountId: String): Flow<DatabaseRelationship>

    @Query(
        "UPDATE relationships " +
                "SET isMuting = :isMuted " +
                "WHERE accountId = :accountId",
    )
    suspend fun updateMuted(
        accountId: String,
        isMuted: Boolean,
    )

    @Query(
        "UPDATE relationships " +
                "SET isBlocking = :isBlocked " +
                "WHERE accountId = :accountId",
    )
    suspend fun updateBlocked(
        accountId: String,
        isBlocked: Boolean,
    )

    @Query(
        "UPDATE relationships " +
                "SET isFollowing = :isFollowing " +
                "WHERE accountId = :accountId",
    )
    suspend fun updateFollowing(
        accountId: String,
        isFollowing: Boolean,
    )

    @Query(
        "DELETE FROM relationships"
    )
    suspend fun deleteAll()

    @Query(
        "DELETE FROM accounts " +
                "WHERE accountId NOT IN (SELECT accountId FROM accounts)"
    )
    suspend fun deleteOldRelationships()

    @Query(
        "UPDATE relationships " +
                "SET isDomainBlocking = :isDomainBlocking " +
                "WHERE accountId IN " +
                "( " +
                    "SELECT accountId from accounts " +
                    "WHERE acct LIKE '%' || :domain || '%' " +
                ")"
    )
    suspend fun updateDomainBlocking(
        domain: String,
        isDomainBlocking: Boolean,
    )
}
