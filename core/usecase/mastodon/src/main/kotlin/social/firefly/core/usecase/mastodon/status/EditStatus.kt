package social.firefly.core.usecase.mastodon.status

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.model.ImageState
import social.firefly.core.model.StatusVisibility
import social.firefly.core.model.request.MediaAttributes
import social.firefly.core.model.request.PollCreate
import social.firefly.core.model.request.StatusCreate
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.StatusRepository
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.usecase.mastodon.R

class EditStatus(
    private val externalScope: CoroutineScope,
    private val statusRepository: StatusRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val timelineRepository: TimelineRepository,
    private val showSnackbar: ShowSnackbar,
    private val dispatcherIo: CoroutineDispatcher = Dispatchers.IO,
) {
    @OptIn(PreferUseCase::class)
    suspend operator fun invoke(
        statusId: String,
        statusText: String,
        imageStates: List<ImageState>,
        visibility: StatusVisibility,
        pollCreate: PollCreate?,
        contentWarningText: String?,
        inReplyToId: String?
    ) = externalScope.async(dispatcherIo) {
        try {
            val status =
                statusRepository.editStatus(
                    statusId = statusId,
                    StatusCreate(
                        status = statusText,
                        mediaIds = if (imageStates.isEmpty()) {
                            null
                        } else {
                            imageStates.mapNotNull { it.attachmentId }
                        },
                        mediaAttributes = imageStates.map {
                            MediaAttributes(
                                id = it.attachmentId!!,
                                description = it.description,
                            )
                        },
                        visibility = visibility,
                        poll = pollCreate,
                        contentWarningText =
                        if (contentWarningText.isNullOrBlank()) {
                            null
                        } else {
                            contentWarningText
                        },
                        inReplyToId = inReplyToId,
                    ),
                )
            saveStatusToDatabase(status)
            timelineRepository.insertStatusIntoTimelines(status)
        } catch (e: Exception) {
            showSnackbar(
                text = StringFactory.resource(R.string.error_editing_post_toast),
                isError = true,
            )
            throw EditStatusFailedException(e)
        }
    }.await()

    class EditStatusFailedException(e: Exception) : Exception(e)
}