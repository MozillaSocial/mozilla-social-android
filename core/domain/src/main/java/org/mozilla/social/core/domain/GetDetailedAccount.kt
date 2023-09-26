package org.mozilla.social.core.domain

import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.model.account.toDatabaseModel
import org.mozilla.social.core.data.repository.model.status.toDatabaseModel
import org.mozilla.social.core.database.SocialDatabase

class GetDetailedAccount(
    private val accountRepository: AccountRepository,
    private val socialDatabase: SocialDatabase,
) {

    suspend operator fun invoke(
        accountId: String,
    ) = withContext(Dispatchers.IO) {
        val accountJob = async { accountRepository.getAccount(accountId) }
        val relationshipJob = async { accountRepository.getAccountRelationships(listOf(accountId)) }
        val account = accountJob.await()
        val relationship = relationshipJob.await()
        socialDatabase.withTransaction {
            socialDatabase.accountsDao().insert(account.toDatabaseModel())
            socialDatabase.relationshipsDao().insert(relationship.toDatabaseModel())
        }
    }
}