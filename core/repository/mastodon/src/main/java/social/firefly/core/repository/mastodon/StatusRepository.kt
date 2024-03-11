package social.firefly.core.repository.mastodon

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import social.firefly.common.annotations.PreferUseCase
import social.firefly.core.database.dao.StatusDao
import social.firefly.core.database.model.entities.DatabaseStatus
import social.firefly.core.model.Context
import social.firefly.core.model.Poll
import social.firefly.core.model.PollVote
import social.firefly.core.model.Status
import social.firefly.core.model.request.StatusCreate
import social.firefly.core.network.mastodon.StatusApi
import social.firefly.core.repository.mastodon.model.context.toExternalModel
import social.firefly.core.repository.mastodon.model.status.toDatabaseModel
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import social.firefly.core.repository.mastodon.model.status.toNetworkModel

class StatusRepository(
    private val api: StatusApi,
    private val dao: StatusDao,
) {
    @PreferUseCase
    suspend fun postStatus(statusCreate: StatusCreate): Status =
        api.postStatus(statusCreate.toNetworkModel()).toExternalModel()

    @PreferUseCase
    suspend fun editStatus(statusId: String, statusCreate: StatusCreate): Status =
        api.editStatus(statusId, statusCreate.toNetworkModel()).toExternalModel()

    @PreferUseCase
    suspend fun voteOnPoll(
        pollId: String,
        pollVote: PollVote,
    ): Poll {
        return api.voteOnPoll(pollId = pollId, body = pollVote.toNetworkModel())
            .toExternalModel()
    }

    @PreferUseCase
    suspend fun boostStatus(statusId: String): Status =
        api.boostStatus(statusId = statusId).toExternalModel()

    suspend fun updateBoostCount(
        statusId: String,
        valueChange: Long,
    ) {
        dao.updateBoostCount(statusId = statusId, valueChange = valueChange)
    }

    suspend fun updateBoosted(
        statusId: String,
        isBoosted: Boolean,
    ) {
        dao.updateBoosted(statusId = statusId, isBoosted = isBoosted)
    }

    @PreferUseCase
    suspend fun unBoostStatus(statusId: String): Status =
        api.unBoostStatus(statusId = statusId).toExternalModel()

    @PreferUseCase
    suspend fun favoriteStatus(statusId: String): Status =
        api.favoriteStatus(statusId = statusId).toExternalModel()

    @PreferUseCase
    suspend fun unFavoriteStatus(statusId: String): Status =
        api.unFavoriteStatus(statusId = statusId).toExternalModel()

    suspend fun getStatusContext(statusId: String): Context {
        return api.getStatusContext(statusId).toExternalModel()
    }

    @PreferUseCase
    suspend fun deleteStatus(statusId: String) {
        api.deleteStatus(statusId)
    }

    suspend fun deleteStatusLocal(statusId: String) {
        dao.deleteStatus(statusId)
    }

    suspend fun deleteOldStatusesFromDatabase() = dao.deleteOldStatuses()

    suspend fun getStatusLocal(statusId: String): Status? {
        val status = dao.getStatus(statusId)
        return status?.toExternalModel()
    }

    fun getStatusesFlow(statusIds: List<String>): Flow<List<Status>> =
        dao.getStatuses(statusIds).map {
            it.map { statusWrapper ->
                statusWrapper.toExternalModel()
            }
        }

    suspend fun updateIsBeingDeleted(
        statusId: String,
        isBeingDeleted: Boolean,
    ) {
        dao.updateIsBeingDeleted(statusId = statusId, isBeingDeleted = isBeingDeleted)
    }

    suspend fun updateFavoriteCount(
        statusId: String,
        valueChange: Long,
    ) {
        dao.updateFavoriteCount(statusId, valueChange)
    }

    suspend fun updateFavorited(
        statusId: String,
        isFavorited: Boolean,
    ) {
        dao.updateFavorited(statusId, isFavorited)
    }

    /**
     * Using this method outside of the use case is dangerous because polls and accounts
     * must be inserted into the database before the status is due to the foreign keys
     * in the statuses table
     */
    @PreferUseCase
    suspend fun insertAll(statuses: List<Status>) {
        dao.upsertAll(statuses.map { it.toDatabaseModel() })
    }
}

 fun Status.toDatabaseModel(): DatabaseStatus =
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
