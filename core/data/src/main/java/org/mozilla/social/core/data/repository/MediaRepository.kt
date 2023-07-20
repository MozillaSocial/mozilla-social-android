package org.mozilla.social.core.data.repository

import org.mozilla.social.core.data.MastodonServiceWrapper
import org.mozilla.social.model.entity.Attachment
import org.mozilla.social.model.entity.request.File
import java.nio.ByteBuffer

class MediaRepository internal constructor(
    private val mastodonServiceWrapper: MastodonServiceWrapper,
) {

    suspend fun uploadImage(
        fileName: String,
        contents: ByteBuffer,
        description: String? = null
    ): Attachment =
        mastodonServiceWrapper.service.value!!.uploadImage(
            file = File(contents, fileName, "image"),
            description = description
        )
}