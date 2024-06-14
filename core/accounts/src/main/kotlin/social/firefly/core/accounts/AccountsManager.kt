package social.firefly.core.accounts

import kotlinx.coroutines.flow.Flow
import social.firefly.core.accounts.dao.MastodonAccountsDao
import social.firefly.core.accounts.model.MastodonAccount

class AccountsManager(
    private val mastodonAccountsDao: MastodonAccountsDao,
) {

    suspend fun getActiveAccount(): MastodonAccount = mastodonAccountsDao.getActiveAccount()

    fun getActiveAccountFlow(): Flow<MastodonAccount> = mastodonAccountsDao.getActiveAccountFlow()
}