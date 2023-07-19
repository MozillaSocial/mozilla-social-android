package org.mozilla.social.core.data.repository

import org.mozilla.social.model.entity.request.StatusCreate

class StatusRepository(
    private val mastodonServiceWrapper: MastodonServiceWrapper,
) {

    suspend fun sendPost(
        statusText: String,
    ) {
        mastodonServiceWrapper.service.value?.postStatus(
            StatusCreate(
                status = statusText,
            )
        )
    }
}