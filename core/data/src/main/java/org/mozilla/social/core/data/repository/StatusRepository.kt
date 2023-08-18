package org.mozilla.social.core.data.repository

import androidx.room.withTransaction
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.data.repository.model.status.toDatabaseModel
import org.mozilla.social.core.data.repository.model.status.toExternalModel
import org.mozilla.social.core.data.repository.model.status.toNetworkModel
import org.mozilla.social.core.database.SocialDatabase
import org.mozilla.social.core.network.MediaApi
import org.mozilla.social.core.network.StatusApi
import org.mozilla.social.core.network.model.NetworkStatusVisibility
import org.mozilla.social.core.network.model.request.NetworkMediaUpdate
import org.mozilla.social.core.network.model.request.NetworkPollCreate
import org.mozilla.social.core.network.model.request.NetworkStatusCreate
import org.mozilla.social.model.ImageState
import org.mozilla.social.model.Status
import org.mozilla.social.model.StatusVisibility
import org.mozilla.social.model.request.PollCreate

class StatusRepository(
    private val statusApi: StatusApi,
    private val mediaApi: MediaApi,
    private val socialDatabase: SocialDatabase,
) {

    suspend fun sendPost(
        statusText: String,
        imageStates: List<ImageState>,
        visibility: StatusVisibility,
        pollCreate: PollCreate?,
        contentWarningText: String?,
        inReplyToId: String?,
    ) {
        coroutineScope {
            // asynchronously update all attachment descriptions before sending post
            imageStates.map { imageState ->
                if (imageState.attachmentId != null && imageState.description.isNotBlank()) {
                    async {
                        mediaApi.updateMedia(
                            imageState.attachmentId!!,
                            NetworkMediaUpdate(imageState.description)
                        )
                    }
                } else {
                    null
                }
            }.forEach {
                it?.await()
            }
            postStatus(
                    status = statusText,
                    mediaIds = if (imageStates.isEmpty()) {
                        null
                    } else {
                        imageStates.mapNotNull { it.attachmentId }
                    },
                    visibility = visibility.toNetworkModel(),
                    poll = pollCreate?.toNetworkModel(),
                    contentWarningText = if (contentWarningText.isNullOrBlank()) {
                        null
                    } else {
                        contentWarningText
                    },
                    inReplyToId = inReplyToId,
                )
        }
    }

    private suspend fun postStatus(
        status: String? = null,
        mediaIds: List<String>? = null,
        visibility: NetworkStatusVisibility? = null,
        poll: NetworkPollCreate? = null,
        contentWarningText: String? = null,
        inReplyToId: String?,
    ) {
        statusApi.postStatus(
            NetworkStatusCreate(
                status = status,
                mediaIds = mediaIds,
                visibility = visibility,
                poll = poll,
                contentWarningText = contentWarningText,
                inReplyToId = inReplyToId,
            )
        )
    }

    suspend fun voteOnPoll(
        pollId: String,
        pollChoices: List<Int>,
    ) {
        socialDatabase.pollDao().updateOwnVotes(pollId, pollChoices)
        try {
            val poll = statusApi.voteOnPoll(
                pollId,
                pollChoices,
            ).toExternalModel()
            socialDatabase.pollDao().update(poll.toDatabaseModel())
        } catch (e: Exception) {
            socialDatabase.pollDao().updateOwnVotes(pollId, null)
            throw e
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

    suspend fun boostStatus(
        statusId: String,
    ) {
        socialDatabase.withTransaction {
            socialDatabase.statusDao().updateBoostCount(statusId, 1)
            socialDatabase.statusDao().updateBoosted(statusId, true)
        }
        try {
            val status = statusApi.boostStatus(statusId).toExternalModel()
            saveStatusToDatabase(status)
        } catch (e: Exception) {
            socialDatabase.withTransaction {
                socialDatabase.statusDao().updateBoostCount(statusId, -1)
                socialDatabase.statusDao().updateBoosted(statusId, false)
            }
            throw e
        }
    }

    suspend fun undoStatusBoost(
        boostedStatusId: String,
    ) {
        socialDatabase.withTransaction {
            socialDatabase.statusDao().updateBoostCount(boostedStatusId, -1)
            socialDatabase.statusDao().updateBoosted(boostedStatusId, false)
        }
        try {
            val status = statusApi.unBoostStatus(boostedStatusId).toExternalModel()
            saveStatusToDatabase(status)
        } catch (e: Exception) {
            socialDatabase.withTransaction {
                socialDatabase.statusDao().updateBoostCount(boostedStatusId, 1)
                socialDatabase.statusDao().updateBoosted(boostedStatusId, true)
            }
            throw e
        }
    }
}