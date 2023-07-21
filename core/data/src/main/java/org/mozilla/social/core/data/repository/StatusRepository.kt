package org.mozilla.social.core.data.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mozilla.social.core.network.MastodonApi
import org.mozilla.social.model.MediaUpdateRequestBody
import org.mozilla.social.model.entity.request.StatusCreate

class StatusRepository(
    private val mastodonApi: MastodonApi,
) {

    /**
     * @param descriptions a map of <attachmentId, description>
     */
    suspend fun sendPost(
        statusText: String,
        attachmentIds: List<String>,
        descriptions: Map<String, String>,
    ) {
        coroutineScope {
            // asynchronously update all attachment descriptions before sending post
            descriptions.map {
                async {
                    mastodonApi.updateMedia(
                        it.key,
                        MediaUpdateRequestBody(it.value)
                    )
                }
            }.forEach {
                it.await()
            }
            mastodonApi.postStatus(
                StatusCreate(
                    status = statusText,
                    mediaIds = attachmentIds
                )
            )
        }
    }
}