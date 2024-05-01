package social.firefly.core.repository.mastodon

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
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
    ): Attachment =
        mediaApi.uploadMedia(
            MultipartBody.Part.createFormData(
                "file",
                file.name,
                file.asRequestBody("image/*".toMediaType()),
            ),
            description,
        ).toExternalModel()

    suspend fun updateMedia(
        mediaId: String,
        mediaUpdate: MediaUpdate,
    ) = mediaApi.updateMedia(mediaId, mediaUpdate.toNetworkModel())
}
