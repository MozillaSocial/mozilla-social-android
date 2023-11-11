package org.mozilla.social.core.repository.mastodon

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mozilla.social.core.repository.mastodon.model.context.toExternalModel
import org.mozilla.social.core.repository.mastodon.model.status.toDatabaseModel
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.network.mastodon.StatusApi
import org.mozilla.social.model.Context
import org.mozilla.social.model.Status

class StatusRepository(
    private val statusApi: StatusApi,
    private val socialDatabase: SocialDatabase,
) {

    suspend fun voteOnPoll(pollId: String, pollVote: PollVote) {
        statusApi.voteOnPoll(pollId = pollId, body = )
    }

    suspend fun getStatusLocal(
        statusId: String
    ): Status? {
        val status = socialDatabase.statusDao().getStatus(statusId)
        return status?.toExternalModel()
    }

    fun getStatusesFlow(
        statusIds: List<String>,
    ): Flow<List<Status>> = socialDatabase.statusDao().getStatuses(statusIds).map {
        it.map { statusWrapper ->
            statusWrapper.toExternalModel()
        }
    }

    suspend fun saveStatusesToDatabase(statuses: List<Status>) {
        saveStatusToDatabase(*statuses.toTypedArray())
    }

    suspend fun saveStatusToDatabase(vararg statuses: Status) {
        socialDatabase.withTransaction {
            val boostedStatuses = statuses.mapNotNull { it.boostedStatus }
            socialDatabase.pollDao().insertAll(boostedStatuses.mapNotNull {
                it.poll?.toDatabaseModel()
            })
            socialDatabase.accountsDao().insertAll(boostedStatuses.map {
                it.account.toDatabaseModel()
            })
            socialDatabase.statusDao().insertAll(boostedStatuses.map {
                it.toDatabaseModel()
            })

            socialDatabase.pollDao().insertAll(statuses.mapNotNull {
                it.poll?.toDatabaseModel()
            })
            socialDatabase.accountsDao().insertAll(statuses.map {
                it.account.toDatabaseModel()
            })
            socialDatabase.statusDao().insertAll(statuses.map {
                it.toDatabaseModel()
            })
        }
    }

    suspend fun getStatusContext(statusId: String): Context =
        statusApi.getStatusContext(statusId).toExternalModel()
}
