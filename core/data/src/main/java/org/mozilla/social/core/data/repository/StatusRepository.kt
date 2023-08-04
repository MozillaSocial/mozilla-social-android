package org.mozilla.social.core.data.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.data.repository.model.toNetworkModel
import org.mozilla.social.core.network.MediaApi
import org.mozilla.social.core.network.StatusApi
import org.mozilla.social.core.network.model.NetworkStatusVisibility
import org.mozilla.social.core.network.model.request.NetworkMediaUpdate
import org.mozilla.social.core.network.model.request.NetworkPollCreate
import org.mozilla.social.core.network.model.request.NetworkStatusCreate
import org.mozilla.social.model.ImageState
import org.mozilla.social.model.StatusVisibility
import org.mozilla.social.model.request.PollCreate

class StatusRepository(
    private val statusApi: StatusApi,
    private val mediaApi: MediaApi,
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
        statusApi.voteOnPoll(
            pollId,
            pollChoices,
        )
    }
}