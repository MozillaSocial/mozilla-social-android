package org.mozilla.social.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.mozilla.social.core.database.model.entities.DatabaseAccount

@Dao
interface AccountsDao : BaseDao<DatabaseAccount> {
    @Query(
        "SELECT * FROM accounts " +
            "WHERE accountId = :accountId",
    )
    suspend fun getAccount(accountId: String): DatabaseAccount?

    @Query(
        "SELECT * FROM accounts " +
            "WHERE accountId = :accountId",
    )
    fun getAccountFlow(accountId: String): Flow<DatabaseAccount>

    @Query(
        "UPDATE accounts " +
            "SET followingCount = followingCount + :valueChange " +
            "WHERE accountId = :accountId",
    )
    suspend fun updateFollowingCount(
        accountId: String,
        valueChange: Long,
    )

    @Query(
        "DELETE FROM accounts " +
        "WHERE accountId NOT IN (:accountIdsToKeep)"
    )
    suspend fun deleteAll(
        accountIdsToKeep: List<String> = emptyList()
    )

    @Query(
        "DELETE FROM accounts " +
        "WHERE accountId NOT IN " +
        "(" +
            "SELECT accountId FROM blocks " +
            "UNION " +
            "SELECT accountId FROM mutes " +
            "UNION " +
            "SELECT followerAccountId FROM followers " +
            "UNION " +
            "SELECT followeeAccountId FROM followings " +
            "UNION " +
            "SELECT accountId FROM statuses " +
            "UNION " +
            "SELECT accountId FROM notifications " +
        ") " +
        "AND accountId NOT IN (:accountIdsToKeep)"
    )
    suspend fun deleteOldAccounts(
        accountIdsToKeep: List<String> = emptyList()
    )
}
