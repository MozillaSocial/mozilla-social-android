package social.firefly.core.accounts.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import social.firefly.core.accounts.model.MastodonAccount

@Dao
interface MastodonAccountsDao : BaseDao<MastodonAccount> {

    @Transaction
    @Query(
        "SELECT * from mastodonAccounts " +
        "WHERE accountId IN " +
        "(" +
            "SELECT accountId FROM activeAccount" +
        ") " +
        "AND " +
        "domain IN " +
        "(" +
            "SELECT domain FROM activeAccount" +
        ") "
    )
    suspend fun getActiveAccount(): MastodonAccount

    @Transaction
    @Query(
        "SELECT * from mastodonAccounts " +
        "WHERE accountId IN " +
        "(" +
            "SELECT accountId FROM activeAccount" +
        ") " +
        "AND " +
        "domain IN " +
        "(" +
            "SELECT domain FROM activeAccount" +
        ") "
    )
    suspend fun getActiveAccountFlow(): Flow<MastodonAccount>
}