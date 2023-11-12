package org.mozilla.social.core.storage.mastodon

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mozilla.social.core.database.dao.AccountsDao
import org.mozilla.social.core.model.Account
import org.mozilla.social.core.storage.mastodon.status.toDatabaseModel
import org.mozilla.social.core.storage.mastodon.status.toExternalModel

class LocalAccountRepository internal constructor(
    private val accountsDao: AccountsDao,
) {
    suspend fun getAccount(accountId: String): Account? {
        return accountsDao.getAccount(accountId)?.toExternalModel()
    }

    fun insertAccount(updatedAccount: Account) {
        accountsDao.insert(updatedAccount.toDatabaseModel())
    }

    fun insertAccounts(accounts: List<Account>) {
        accountsDao.insertAll(accounts.map { it.toDatabaseModel() })
    }

    fun getAccountFlow(accountId: String): Flow<Account> {
        return accountsDao.getAccountFlow(accountId = accountId).map { it.toExternalModel() }
    }
}