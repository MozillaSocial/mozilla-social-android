package org.mozilla.social.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import org.mozilla.social.core.database.model.DatabaseAccount

@Dao
interface AccountsDao : BaseDao<DatabaseAccount> {

    @Query(
        "SELECT * FROM accounts " +
        "WHERE accountId = :accountId"
    )
    suspend fun getAccount(accountId: String): DatabaseAccount?
}