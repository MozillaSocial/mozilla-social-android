package org.mozilla.social.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.mozilla.social.core.database.model.DatabaseAccount

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
}
