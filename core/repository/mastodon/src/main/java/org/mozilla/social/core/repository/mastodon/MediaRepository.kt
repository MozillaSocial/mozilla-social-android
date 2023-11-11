package org.mozilla.social.core.repository.mastodon

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.mozilla.social.core.repository.mastodon.model.status.toExternalModel
import org.mozilla.social.core.network.mastodon.MediaApi
import org.mozilla.social.model.Attachment
import java.io.File

class MediaRepository internal constructor(
    private val mediaApi: MediaApi,
) {

    suspend fun uploadImage(
        file: File,
        description: String?,
    ): Attachment = mediaApi.uploadMedia(
        MultipartBody.Part.createFormData(
            "file",
            file.name,
            file.asRequestBody("image/*".toMediaType()),
        ),
        description,
    ).toExternalModel()
}