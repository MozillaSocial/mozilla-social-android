package org.mozilla.social.core.usecase.mastodon.status

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.mozilla.social.common.annotations.PreferUseCase
import org.mozilla.social.common.utils.StringFactory
import org.mozilla.social.core.repository.mastodon.StatusRepository
import org.mozilla.social.core.repository.mastodon.TimelineRepository
import org.mozilla.social.core.navigation.usecases.ShowSnackbar
import org.mozilla.social.core.repository.mastodon.MediaRepository
import org.mozilla.social.core.model.MediaUpdate
import org.mozilla.social.core.usecase.mastodon.R
import org.mozilla.social.core.model.ImageState
import org.mozilla.social.core.model.StatusVisibility
import org.mozilla.social.core.model.request.PollCreate
import org.mozilla.social.core.model.request.StatusCreate

class PostStatus internal constructor(
    private val externalScope: CoroutineScope,
    private val mediaApi: MediaRepository,
    private val statusRepository: StatusRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val timelineRepository: TimelineRepository,
    private val showSnackbar: ShowSnackbar,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {

    @OptIn(PreferUseCase::class)
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
                            MediaUpdate(
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

            val status = statusRepository.postStatus(
                StatusCreate(
                    status = statusText,
                    mediaIds = if (imageStates.isEmpty()) {
                        null
                    } else {
                        imageStates.mapNotNull { it.attachmentId }
                    },
                    visibility = visibility,
                    poll = pollCreate,
                    contentWarningText = if (contentWarningText.isNullOrBlank()) {
                        null
                    } else {
                        contentWarningText
                    },
                    inReplyToId = inReplyToId,
                )
            )
            saveStatusToDatabase(status)
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