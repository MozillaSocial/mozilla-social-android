package social.firefly.core.accounts

import kotlinx.coroutines.flow.Flow
import social.firefly.core.accounts.dao.ActiveAccountDao
import social.firefly.core.accounts.dao.MastodonAccountsDao
import social.firefly.core.accounts.model.AccountType
import social.firefly.core.accounts.model.ActiveAccount
import social.firefly.core.accounts.model.MastodonAccount

class AccountsManager(
    private val mastodonAccountsDao: MastodonAccountsDao,
    private val activeAccountDao: ActiveAccountDao,
) {

    suspend fun getActiveAccount(): MastodonAccount = mastodonAccountsDao.getActiveAccount()

    fun getActiveAccountFlow(): Flow<MastodonAccount> = mastodonAccountsDao.getActiveAccountFlow()

    suspend fun getAllAccounts(): List<MastodonAccount> = mastodonAccountsDao.getAllAccounts()

    fun getAllAccountsFlow(): Flow<List<MastodonAccount>> =
        mastodonAccountsDao.getAllAccountsFlow()

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
        activeAccountDao.upsert(
            ActiveAccount(
                accountType = AccountType.MASTODON,
                accountId = accountId,
                domain = domain,
            )
        )
    }

    suspend fun deleteAccount(
        accountId: String,
        domain: String,
    ) {
        val activeAccount = mastodonAccountsDao.getActiveAccount()
        val accounts = mastodonAccountsDao.getAllAccounts()
        if (accountId == activeAccount.accountId && domain == activeAccount.domain) {
            val newActiveAccount = (accounts - activeAccount).firstOrNull()
            if (newActiveAccount != null) {
                activeAccountDao.upsert(
                    ActiveAccount(
                        accountType = AccountType.MASTODON,
                        accountId = newActiveAccount.accountId,
                        domain = newActiveAccount.domain,
                    )
                )
            } else {
                activeAccountDao.removeActiveAccount()
            }
        }
        mastodonAccountsDao.deleteAccount(
            accountId = accountId,
            domain = domain,
        )
    }

    suspend fun deleteAllAccounts() {
        mastodonAccountsDao.deleteAllAccounts()
    }

    suspend fun updateAccountInfo(
        mastodonAccount: MastodonAccount,
        avatarUrl: String,
        username: String,
        defaultLanguage: String,
    ) = mastodonAccountsDao.upsert(
        mastodonAccount.copy(
            avatarUrl = avatarUrl,
            userName = username,
            defaultLanguage = defaultLanguage,
        )
    )

    suspend fun updatePushKeys(
        mastodonAccount: MastodonAccount,
        serializedPushKeys: String,
    ) {
        mastodonAccountsDao.upsert(
            mastodonAccount.copy(
                serializedPushKeys = serializedPushKeys
            )
        )
    }

    suspend fun setActiveAccount(
        mastodonAccount: MastodonAccount,
    ) {
        activeAccountDao.upsert(
            ActiveAccount(
                accountType = AccountType.MASTODON,
                accountId = mastodonAccount.accountId,
                domain = mastodonAccount.domain,
            )
        )
    }

    suspend fun updateLastSeenHomeStatusId(
        mastodonAccount: MastodonAccount,
        lastSeenStatusId: String,
    ) {
        mastodonAccountsDao.upsert(
            mastodonAccount.copy(
                lastSeenHomeStatusId = lastSeenStatusId,
            )
        )
    }
}