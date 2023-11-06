package org.mozilla.social.core.domain

import androidx.room.withTransaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import org.mozilla.social.common.Resource
import org.mozilla.social.common.utils.launchSupervisor
import org.mozilla.social.core.data.repository.AccountRepository
import org.mozilla.social.core.data.repository.model.account.toDatabaseModel
import org.mozilla.social.core.data.repository.model.account.toExternal
import org.mozilla.social.core.data.repository.model.status.toDatabaseModel
import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.model.Account
import org.mozilla.social.model.Relationship
import timber.log.Timber

class GetDetailedAccount(
    private val accountRepository: AccountRepository,
    private val socialDatabase: SocialDatabase,
) {

    /**
     * @param transform used to transform an account and relationship into a single model, likely
     * a UI state model of some kind
     */
    operator fun <T> invoke(
        accountId: String,
        coroutineScope: CoroutineScope,
        transform: (account: Account, relationship: Relationship) -> T,
    ): Flow<Resource<T>> = flow {
        emit(Resource.Loading())
        val mutex = Mutex(true)
        var exception: Exception? = null
        coroutineScope.launchSupervisor {
            try {
                val accountJob = async { accountRepository.getAccount(accountId) }
                val relationshipJob =
                    async { accountRepository.getAccountRelationships(listOf(accountId)) }
                val account = accountJob.await()
                val relationship = relationshipJob.await()
                socialDatabase.withTransaction {
                    socialDatabase.accountsDao().insert(account.toDatabaseModel())
                    socialDatabase.relationshipsDao().insert(relationship.first().toDatabaseModel())
                }
            } catch (e: Exception) {
                Timber.e(e)
                exception = e
            } finally {
                mutex.unlock()
            }
        }

        // wait to emit until after we have grabbed everything stored in the database
        mutex.lock()

        exception?.let {
            emit(Resource.Error(it))
        } ?: try {
            emitAll(
                socialDatabase.accountsDao().getAccountFlow(accountId).filterNotNull().combine(
                    socialDatabase.relationshipsDao().getRelationshipFlow(accountId).filterNotNull()
                ) { databaseAccount, databaseRelationship ->
                    Resource.Loaded(
                        transform(
                            databaseAccount.toExternalModel(),
                            databaseRelationship.toExternal()
                        )
                    )
                }
            )
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }
}