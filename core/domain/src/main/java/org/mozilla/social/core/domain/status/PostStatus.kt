package org.mozilla.social.core.domain.status

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.repository.mastodon.TimelineRepository
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel
import org.mozilla.social.core.repository.mastodon.model.status.toNetworkModel
import org.mozilla.social.core.domain.R
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.network.mastodon.MediaApi
import org.mozilla.social.core.network.mastodon.StatusApi
import org.mozilla.social.core.network.mastodon.model.request.NetworkMediaUpdate
import org.mozilla.social.core.network.mastodon.model.request.NetworkStatusCreate
import org.mozilla.social.model.ImageState
import org.mozilla.social.model.StatusVisibility
import org.mozilla.social.model.request.PollCreate

class PostStatus(
    private val externalScope: CoroutineScope,
    private val statusApi: org.mozilla.social.core.network.mastodon.StatusApi,
    private val mediaApi: org.mozilla.social.core.network.mastodon.MediaApi,
    private val statusRepository: StatusRepository,
    private val timelineRepository: TimelineRepository,
    private val showSnackbar: ShowSnackbar,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend operator fun invoke(
        statusText: String,
        imageStates: List<ImageState>,
        visibility: StatusVisibility,
        pollCreate: PollCreate?,
        contentWarningText: String?,
        inReplyToId: String?,
    ) = externalScope.async(dispatcherIo) {
        try {
            // asynchronously update all attachment descriptions before sending post
            imageStates.map { imageState ->
                if (imageState.attachmentId != null && imageState.description.isNotBlank()) {
                    async {
                        mediaApi.updateMedia(
                            imageState.attachmentId!!,
                            org.mozilla.social.core.network.mastodon.model.request.NetworkMediaUpdate(
                                imageState.description
                            )
                        )
                    }
                } else {
                    null
                }
            }.forEach {
                it?.await()
            }

            val status = statusApi.postStatus(
                org.mozilla.social.core.network.mastodon.model.request.NetworkStatusCreate(
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
            ).toExternalModel()

            statusRepository.saveStatusToDatabase(status)
            timelineRepository.insertStatusIntoTimelines(status)
        } catch (e: Exception) {
            showSnackbar(
                text = StringFactory.resource(R.string.error_sending_post_toast),
                isError = true,
            )
            throw PostStatusFailedException(e)
        }
    }.await()

    class PostStatusFailedException(e: Exception) : Exception(e)
}