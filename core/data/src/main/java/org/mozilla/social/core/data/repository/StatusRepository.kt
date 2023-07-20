package org.mozilla.social.core.data.repository

import org.mozilla.social.core.network.MastodonApi
import org.mozilla.social.model.entity.request.StatusCreate

class StatusRepository(
    private val mastodonApi: MastodonApi,
) {

    suspend fun sendPost(
        statusText: String,
        attachmentId: String?,
    ) {
        mastodonApi.postStatus(
            StatusCreate(
                status = statusText,
                mediaIds = buildList {
                    attachmentId?.let { add(it) }
                }
            )
        )
//        mastodonServiceWrapper.service.value?.postStatus(
//            StatusCreate(
//                status = statusText,
//                mediaIds = buildList {
//                    attachmentId?.let { add(it) }
//                }
//            )
//        )
    }
}