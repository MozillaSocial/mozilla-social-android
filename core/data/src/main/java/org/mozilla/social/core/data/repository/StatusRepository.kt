package org.mozilla.social.core.data.repository

import org.mozilla.social.core.data.MastodonServiceWrapper
import org.mozilla.social.model.entity.request.StatusCreate

class StatusRepository(
    private val mastodonServiceWrapper: MastodonServiceWrapper,
) {

    suspend fun sendPost(
        statusText: String,
        attachmentId: String?,
    ) {
        mastodonServiceWrapper.service.value?.postStatus(
            StatusCreate(
                status = statusText,
                mediaIds = buildList {
                    attachmentId?.let { add(it) }
                }
            )
        )
    }
}