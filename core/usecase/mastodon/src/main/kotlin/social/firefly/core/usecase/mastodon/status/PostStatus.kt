package social.firefly.core.usecase.mastodon.status

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import social.firefly.common.annotations.PreferUseCase
import social.firefly.common.utils.StringFactory
import social.firefly.core.model.ImageState
import social.firefly.core.model.StatusVisibility
import social.firefly.core.model.exceptions.HttpException
import social.firefly.core.model.request.PollCreate
import social.firefly.core.model.request.StatusCreate
import social.firefly.core.navigation.usecases.ShowSnackbar
import social.firefly.core.repository.mastodon.MediaRepository
import social.firefly.core.repository.mastodon.StatusRepository
import social.firefly.core.repository.mastodon.TimelineRepository
import social.firefly.core.usecase.mastodon.R
import social.firefly.core.usecase.mastodon.thread.GetThread

class PostStatus internal constructor(
    private val externalScope: CoroutineScope,
    private val mediaApi: MediaRepository,
    private val statusRepository: StatusRepository,
    private val saveStatusToDatabase: SaveStatusToDatabase,
    private val timelineRepository: TimelineRepository,
    private val getThread: GetThread,
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
        languageCode: String?,
    ) = externalScope.async(dispatcherIo) {
        try {
            // asynchronously update all attachment descriptions before sending post
            // for some reason when posting, adding media attachment fields doesn't work
            imageStates.map { imageState ->
                if (imageState.attachmentId != null && imageState.description.isNotBlank()) {
                    async {
                        mediaApi.updateMedia(
                            mediaId = imageState.attachmentId!!,
                            description = imageState.description,
                        )
                    }
                } else {
                    null
                }
            }.forEach {
                it?.await()
            }

            val status =
                statusRepository.postStatus(
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
                        language = languageCode,
                    ),
                )
            saveStatusToDatabase(status)
            getThread.pushNewStatus(status.statusId)
            timelineRepository.insertStatusIntoTimelines(status)
        } catch (e: HttpException) {
            showSnackbar(
                text = StringFactory.literal(e.errorMessage),
                isError = true,
            )
            throw PostStatusFailedException(e)
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
