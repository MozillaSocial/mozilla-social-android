package org.mozilla.social.core.database.dao

import android.accounts.Account
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.mozilla.social.core.database.model.DatabaseAccount

@Dao
interface AccountsDao : BaseDao<DatabaseAccount> {

    @Query(
        "SELECT * FROM accounts " +
        "WHERE accountId = :accountId"
    )
    suspend fun getAccount(accountId: String): DatabaseAccount?

    @Query(
        "SELECT * FROM accounts " +
        "WHERE accountId = :accountId"
    )
    fun getAccountFlow(accountId: String): Flow<DatabaseAccount>
}