package social.firefly.core.repository.mastodon

import social.firefly.core.model.Attachment
import social.firefly.core.model.MediaUpdate
import social.firefly.core.network.mastodon.MediaApi
import social.firefly.core.repository.mastodon.model.status.toExternalModel
import social.firefly.core.repository.mastodon.model.status.toNetworkModel
import java.io.File

class MediaRepository internal constructor(
    private val mediaApi: MediaApi,
) {
    suspend fun uploadMedia(
        file: File,
        description: String?,
    ): Attachment = mediaApi.uploadMedia(
        file = file,
        description = description,
    ).toExternalModel()

    suspend fun updateMedia(
        mediaId: String,
        description: String?,
    ) = mediaApi.updateMedia(
        mediaId = mediaId,
        description = description
    )
}
