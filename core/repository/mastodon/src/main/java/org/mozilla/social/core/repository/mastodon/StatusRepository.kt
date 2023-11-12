package org.mozilla.social.core.repository.mastodon

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mozilla.social.core.network.mastodon.StatusApi
import org.mozilla.social.core.model.PollVote
import org.mozilla.social.core.repository.mastodon.model.context.toExternalModel
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel
import org.mozilla.social.core.repository.mastodon.model.status.toNetworkModel
import org.mozilla.social.core.model.Context
import org.mozilla.social.core.model.Poll
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.model.request.StatusCreate

class StatusRepository(
    private val statusApi: StatusApi,
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

    // TODO@DA move to use case

}
