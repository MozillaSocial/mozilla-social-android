package social.firefly.core.network.mastodon

import social.firefly.core.network.mastodon.model.responseBody.NetworkAttachment
import java.io.File

interface MediaApi {
    suspend fun uploadMedia(
        file: File,
        description: String? = null,
    ): NetworkAttachment

    suspend fun updateMedia(
        mediaId: String,
        description: String?,
    )
}
