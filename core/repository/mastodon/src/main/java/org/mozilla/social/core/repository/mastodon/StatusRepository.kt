package org.mozilla.social.core.repository.mastodon

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mozilla.social.core.database.dao.StatusDao
import org.mozilla.social.core.database.model.DatabaseStatus
import org.mozilla.social.core.model.Context
import org.mozilla.social.core.model.Poll
import org.mozilla.social.core.model.PollVote
import org.mozilla.social.core.model.Status
import org.mozilla.social.core.model.request.StatusCreate
import org.mozilla.social.core.network.mastodon.StatusApi
import org.mozilla.social.core.repository.mastodon.model.context.toExternalModel
import org.mozilla.social.core.repository.mastodon.model.status.toDatabaseModel
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel
import org.mozilla.social.core.repository.mastodon.model.status.toNetworkModel

class StatusRepository(
    private val api: StatusApi,
    private val dao: StatusDao,
) {
    suspend fun postStatus(statusCreate: StatusCreate): Status =
        api.postStatus(statusCreate.toNetworkModel()).toExternalModel()

    suspend fun voteOnPoll(pollId: String, pollVote: PollVote): Poll {
        return api.voteOnPoll(pollId = pollId, body = pollVote.toNetworkModel())
            .toExternalModel()
    }

    suspend fun boostStatus(statusId: String): Status =
        api.boostStatus(statusId = statusId).toExternalModel()

    suspend fun updateBoostCount(statusId: String, valueChange: Long) {
        dao.updateBoostCount(statusId = statusId, valueChange = valueChange)
    }

    suspend fun updateBoosted(statusId: String, isBoosted: Boolean) {
        dao.updateBoosted(statusId = statusId, isBoosted = isBoosted)
    }

    suspend fun unBoostStatus(statusId: String): Status =
        api.unBoostStatus(statusId = statusId).toExternalModel()

    suspend fun favoriteStatus(statusId: String): Status =
        api.favoriteStatus(statusId = statusId).toExternalModel()

    suspend fun unFavoriteStatus(statusId: String): Status =
        api.unFavoriteStatus(statusId = statusId).toExternalModel()

    suspend fun getStatusContext(statusId: String): Context {
        return api.getStatusContext(statusId).toExternalModel()
    }

    suspend fun deleteStatus(statusId: String) {
        api.deleteStatus(statusId)
    }

    suspend fun getStatusLocal(
        statusId: String
    ): Status? {
        val status = dao.getStatus(statusId)
        return status?.toExternalModel()
    }

    fun getStatusesFlow(
        statusIds: List<String>,
    ): Flow<List<Status>> = dao.getStatuses(statusIds).map {
        it.map { statusWrapper ->
            statusWrapper.toExternalModel()
        }
    }

    suspend fun updateIsBeingDeleted(statusId: String, isBeingDeleted: Boolean) {
        dao.updateIsBeingDeleted(statusId = statusId, isBeingDeleted = isBeingDeleted)
    }

    suspend fun deleteStatusLocal(statusId: String) {
        dao.deleteStatus(statusId)
    }

    suspend fun updateFavoriteCount(statusId: String, valueChange: Long) {
        dao.updateFavoriteCount(statusId, valueChange)
    }

    suspend fun updateFavorited(statusId: String, isFavorited: Boolean) {
        dao.updateFavorited(statusId, isFavorited)
    }

    fun insertAll(statuses: List<Status>) {
        dao.insertAll(statuses.map { it.toDatabaseModel() })
    }

    fun insertAll(vararg statuses: Status) {
        insertAll(statuses.asList())
    }
}

private fun Status.toDatabaseModel(): DatabaseStatus =
    DatabaseStatus(
        statusId = statusId,
        uri = uri,
        createdAt = createdAt,
        accountId = account.accountId,
        content = content,
        visibility = visibility.toDatabaseModel(),
        isSensitive = isSensitive,
        contentWarningText = contentWarningText,
        mediaAttachments = mediaAttachments.map { it.toDatabaseModel() },
        mentions = mentions.map { it.toDatabaseModel() },
        hashTags = hashTags.map { it.toDatabaseModel() },
        emojis = emojis.map { it.toDatabaseModel() },
        boostsCount = boostsCount,
        favouritesCount = favouritesCount,
        repliesCount = repliesCount,
        application = application?.toDatabaseModel(),
        url = url,
        inReplyToId = inReplyToId,
        inReplyToAccountId = inReplyToAccountId,
        inReplyToAccountName = inReplyToAccountName,
        boostedStatusId = boostedStatus?.statusId,
        boostedStatusAccountId = boostedStatus?.account?.accountId,
        pollId = poll?.pollId,
        card = card?.toDatabaseModel(),
        language = language,
        plainText = plainText,
        isFavorited = isFavourited,
        isBoosted = isBoosted,
        isMuted = isMuted,
        isBookmarked = isBookmarked,
        isPinned = isPinned,
        isBeingDeleted = isBeingDeleted,
    )
