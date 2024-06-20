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
    fun getActiveAccountFlow(): Flow<MastodonAccount>

    @Query(
        "SELECT * from mastodonAccounts"
    )
    suspend fun getAllAccounts(): List<MastodonAccount>

    @Query(
        "SELECT * from mastodonAccounts"
    )
    fun getAllAccountsFlow(): Flow<MastodonAccount>

    @Query(
        "DELETE from mastodonAccounts " +
        "WHERE accountId = :accountId " +
        "AND domain = :domain"
    )
    suspend fun deleteAccount(
        accountId: String,
        domain: String,
    )

    @Query(
        "DELETE FROM mastodonAccounts"
    )
    suspend fun deleteAllAccounts()
}