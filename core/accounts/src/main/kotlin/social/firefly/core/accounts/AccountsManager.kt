package social.firefly.core.accounts

import kotlinx.coroutines.flow.Flow
import social.firefly.core.accounts.dao.MastodonAccountsDao
import social.firefly.core.accounts.model.MastodonAccount

class AccountsManager(
    private val mastodonAccountsDao: MastodonAccountsDao,
) {

    suspend fun getActiveAccount(): MastodonAccount = mastodonAccountsDao.getActiveAccount()

    fun getActiveAccountFlow(): Flow<MastodonAccount> = mastodonAccountsDao.getActiveAccountFlow()

    suspend fun createNewMastodonUser(
        domain: String,
        accessToken: String,
        accountId: String,
        userName: String,
        avatarUrl: String,
        defaultLanguage: String,
    ) {
        mastodonAccountsDao.upsert(
            MastodonAccount(
                accessToken = accessToken,
                accountId = accountId,
                userName = userName,
                avatarUrl = avatarUrl,
                domain = domain,
                defaultLanguage = defaultLanguage,
            )
        )

    }
}