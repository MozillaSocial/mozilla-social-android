package org.mozilla.social.core.repository.mastodon

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.database.datasource.PollLocalDataSource
import org.mozilla.social.core.network.mastodon.StatusApi
import org.mozilla.social.core.model.PollVote
import org.mozilla.social.core.repository.mastodon.model.context.toExternalModel
import org.mozilla.social.core.repository.mastodon.model.status.toDatabaseModel
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel
import org.mozilla.social.core.repository.mastodon.model.status.toNetworkModel
import org.mozilla.social.core.model.Context
import org.mozilla.social.core.model.Poll
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.model.request.StatusCreate

class StatusRepository(
    private val statusApi: StatusApi,
    private val socialDatabase: SocialDatabase,
) {
    suspend fun postStatus(statusCreate: StatusCreate): Status =
        statusApi.postStatus(statusCreate.toNetworkModel()).toExternalModel()

    suspend fun voteOnPoll(pollId: String, pollVote: PollVote): Poll {
        return statusApi.voteOnPoll(pollId = pollId, body = pollVote.toNetworkModel())
            .toExternalModel()
    }

    suspend fun boostStatus(statusId: String): Status =
        statusApi.boostStatus(statusId = statusId).toExternalModel()

    suspend fun unBoostStatus(statusId: String): Status =
        statusApi.unBoostStatus(statusId = statusId).toExternalModel()

    suspend fun favoriteStatus(statusId: String): Status =
        statusApi.favoriteStatus(statusId = statusId).toExternalModel()

    suspend fun unFavoriteStatus(statusId: String): Status =
        statusApi.unFavoriteStatus(statusId = statusId).toExternalModel()

    suspend fun getStatusContext(statusId: String): Context {
        return statusApi.getStatusContext(statusId).toExternalModel()
    }

    suspend fun deleteStatus(statusId: String) {
        statusApi.deleteStatus(statusId)
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
}