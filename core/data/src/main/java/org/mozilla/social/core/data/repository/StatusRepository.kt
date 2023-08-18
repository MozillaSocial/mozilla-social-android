package org.mozilla.social.core.data.repository

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
import org.mozilla.social.model.Poll
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

    suspend fun getStatusLocal(
        statusId: String
    ): Status? {
        val status = socialDatabase.statusDao().getStatus(statusId)
        return status?.toExternalModel()
    }
}