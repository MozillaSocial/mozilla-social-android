package org.mozilla.social.core.data.repository

import org.mozilla.social.core.network.MastodonApi
import org.mozilla.social.model.entity.request.StatusCreate

class StatusRepository(
    private val mastodonApi: MastodonApi,
) {

    suspend fun sendPost(
        statusText: String,
        attachmentIds: List<String>,
    ) {
        mastodonApi.postStatus(
            StatusCreate(
                status = statusText,
                mediaIds = attachmentIds
            )
        )
    }
}